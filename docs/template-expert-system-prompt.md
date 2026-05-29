# 专家模型 — 预编码模板生成指令

## 任务

给定一个多模态 API 的文档链接，提取该 API 的请求参数和响应格式，输出一个符合下面 schema 的预编码模板 JSON。

## 模板结构

```json
{
  "requestBody": {
    "t2i": { /* 文生图（无参考图）的请求体 */ },
    "i2i": { /* 图生图（有参考图）的请求体 */ }
  },
  "paramsSchema": { /* 前端控件描述 */ },
  "responsePath": {
    "default": "JSON Path to image URL in t2i response",
    "i2i": "JSON Path to image URL in i2i response (if different)"
  },
  "costFormula": {
    "base": 50,
    "modifiers": { /* 参数级加价规则 */ }
  }
}
```

## 占位符约定

模板中用 `${}` 表示运行时替换的值。**所有 `${}` 必须放在 JSON 字符串值中（带双引号）。**

| 占位符 | 来源 | 说明 |
|--------|------|------|
| `${model}` | 配置表 | 模型标识，请求体里的 model 字段 |
| `${prompt}` | 用户输入 | 文本提示词 |
| `${params.xxx}` | 前端传参 | 用户选择的参数（如 size、thinkingMode） |
| `${params.refImageUrls[0]}` | 前端传参 | 参考图 URL，i2i 专用，t2i 不存在 |

**类型处理规则（引擎自动后处理）：**
- `${expr}` 替换后，引擎会做类型收窄：
  - 字符串 `"true"` / `"false"` → 布尔值 `true` / `false`
  - 纯数字字符串 `"58"` → 数字 `58`
  - 其他 → 保持字符串
- 所以 `"thinking_mode": "${params.thinkingMode ?? true}"` → 引擎评估结果 → `"thinking_mode": true`（布尔）
- 支持 `||` 和 `??` 默认值：`"${params.size || '2K'}"`

**为什么全用字符串形式而不在 JSON 中写裸 `${}`？**
因为模板本身必须是合法 JSON（方便存储、校验、预览）。所有占位符替换在字符串层完成后再 JSON.parse。

## requestBody 编写规则

1. **必须同时提供 t2i 和 i2i 两个路径**
2. 区别只在 i2i 比 t2i 多一个 `image` 字段（参考图 URL），以及某些仅在 t2i 生效的参数（如 thinking_mode）在 i2i 中设为固定值
3. 请求体里的常量值直接写死（如 `"response_format": "url"`、`"watermark": false`、`"n": 1`）
4. 大小写、字段命名严格遵循 API 文档的原始大小写
5. **可配 vs 硬编码的判断标准：** 该参数值是否随每次请求变化？是否值得暴露给用户调节？
   - 会变且用户应该能改的 → 暴露到 paramsSchema + 用 `${}` 占位
   - 固定值或不希望用户改的 → 直接写死在模板里，不出现在 paramsSchema
   - 例如 `n`（生成张数）如果受限于业务（画廊/OSS），就硬编码 `"n": 1`

## paramsSchema 编写规则

每个参数描述包含：

| 字段 | 类型 | 说明 |
|------|------|------|
| `type` | string | 控件类型：`select` / `switch` / `number` / `text` |
| `label` | string | 中文显示名 |
| `options` | array | select 类型时，选项列表（带清晰描述） |
| `default` | any | 默认值，与 API 文档一致 |

**选择原则：**
- 只暴露有意义给用户调节的参数。技术性参数（如 stream、response_format）不暴露
- 参数名用 camelCase（前端约定），与 API 文档的 snake_case 不同没关系
- 选项的描述要让人能看懂，如 `"1K (1024×1024)"` 而不是 `"1K"`
- 取 API 文档的默认值作为 paramsSchema 的 default

## responsePath 编写规则

使用 JSON Path（点号语法）定位图片 URL 在响应中的位置：

```
$.output[0].content[0].text
```

- t2i 和 i2i 的提取路径不同时，才在 `responsePath.i2i` 单独写
- 如果路径相同，只写 `default`

## costFormula 编写规则

```json
{
  "base": <整数>,
  "modifiers": {
    "参数名": { "参数值": 加价 },
    "i2i": <整数>
  }
}
```

- `base`：该模型的基础调用成本（最低配置：最小分辨率 × 1张 × 无附加功能）
- `modifiers`：按参数值加价，最终 cost = base + Σ(各 modifiers 匹配值)
- 常量 `"i2i"` 表示 i2i 模式额外加价（如果与 t2i 同价则不写）
- `switch` 类型的参数 → modifiers 用 `{"true": 加价, "false": 0}` 格式
- 不涉及价格的参数不要加到 costFormula（如仅用于前端控件的 `seed`）

## 示例

见已有模板：`wan2.7-image` 和 `doubao-seedream-5.0-lite`，它们就是按照本文档生成的标准模板。

## 约束

- 不要猜测 API 不支持的参数
- 如果 API 文档中某个参数信息不完整，标注为可选并用 API 默认值
- 如果 i2i 的请求格式在文档中未说明，i2i 路径就只在 t2i 基础上加 `"image": "${params.refImageUrls[0]}"`
- 响应路径必须从实际响应示例中确认，不要猜测

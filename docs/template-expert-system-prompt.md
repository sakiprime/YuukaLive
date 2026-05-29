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

模板中用 `${}` 表示运行时替换的值：

| 占位符 | 来源 | 说明 |
|--------|------|------|
| `${model}` | 配置表 | 模型标识，请求体里的 model 字段 |
| `${prompt}` | 用户输入 | 文本提示词 |
| `${params.xxx}` | 前端传参 | 用户选择的参数（如 size、n） |
| `${params.refImageUrls[0]}` | 前端传参 | 参考图 URL，i2i 专用，t2i 不存在 |

**类型处理规则：**
- `${}` 出现在字符串值中 → 替换后仍然字符串：`"size": "${params.size}"`
- `${}` 单独作为值（无引号包裹）→ 替换后为原始类型：
  - `"n": ${params.n}` → `"n": 4`（数字）
  - `"thinking_mode": ${params.flag}` → `"thinking_mode": true`（布尔）
- 运算式：`${params.size || '2K'}` → 支持 `||` 默认值
- 空值合并：`${params.n ?? 4}` → 支持 `??` 默认值

## requestBody 编写规则

1. **必须同时提供 t2i 和 i2i 两个路径**
2. 区别只在 i2i 比 t2i 多一个 `image` 字段（参考图 URL），以及某些仅在 t2i 生效的参数（如 thinking_mode）要设为 false
3. 请求体里的常量值直接写死（如 `"response_format": "url"`、`"watermark": false`）
4. 大小写、字段命名严格遵循 API 文档的原始大小写

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
    "i2i": <整数>  // i2i 模式固定加价
  }
}
```

- `base`：该模型的基础调用成本
- `modifiers`：按参数值加价，最终 = base + sum(modifiers)
- 常量 `"i2i"` 表示 i2i 模式额外加价（如果与 t2i 同价则不写）
- 不涉及价格的参数不要加到 costFormula

## 示例

见已有模板：`wan2.7-image` 和 `doubao-seedream-5.0-lite`，它们就是按照本文档生成的标准模板。

## 约束

- 不要猜测 API 不支持的参数
- 如果 API 文档中某个参数信息不完整，标注为可选并用 API 默认值
- 如果 i2i 的请求格式在文档中未说明，i2i 路径就只在 t2i 基础上加 `"image": "${params.refImageUrls[0]}"`
- 响应路径必须从实际响应示例中确认，不要猜测

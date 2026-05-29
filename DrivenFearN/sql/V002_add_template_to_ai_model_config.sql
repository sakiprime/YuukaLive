-- 给 ai_model_config 表添加 template 字段，用于预编码请求体模板
ALTER TABLE ai_model_config
    ADD COLUMN template TEXT COMMENT '请求体模板JSON: {requestBody, paramsSchema, responsePath}' AFTER status;

-- 更新 wan2.7-image 模板
UPDATE ai_model_config SET template = '{
  "requestBody": {
    "model": "${model}",
    "response_format": "url",
    "watermark": false,
    "input": {
      "messages": [
        {
          "role": "user",
          "content": [
            {
              "type": "text",
              "text": "${prompt}"
            }
          ]
        }
      ]
    },
    "parameters": {
      "size": "${params.size || ''2K''}",
      "n": 1
    }
  },
  "paramsSchema": {
    "size": {
      "type": "select",
      "label": "分辨率",
      "options": ["1K", "2K", "4K"],
      "default": "2K"
    }
  },
  "responsePath": {
    "default": "$.output[0].content[0].text"
  }
}' WHERE model_name = 'wan2.7-image';

-- 更新 doubao-seedream-5.0-lite 模板
UPDATE ai_model_config SET template = '{
  "requestBody": {
    "model": "${model}",
    "response_format": "url",
    "watermark": false,
    "input": "${prompt}",
    "size": "${params.size || ''2K''}"
  },
  "paramsSchema": {
    "size": {
      "type": "select",
      "label": "分辨率",
      "options": ["1K", "2K"],
      "default": "2K"
    }
  },
  "responsePath": {
    "default": "$.output[0].content[0].text",
    "i2i": "$.output[0].image_url.url"
  }
}' WHERE model_name = 'doubao-seedream-5.0-lite';

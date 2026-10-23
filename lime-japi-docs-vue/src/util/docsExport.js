/**
 * 文档导出工具
 * 统一处理OpenAPI预览、文件下载和文件名解析
 */

const EXPORT_BASE_URL = '/lime_japi_docs/api/docs/export'

/**
 * 构建导出地址
 *
 * @param format 导出格式 openapi/markdown
 * @param params 导出参数
 * @param download 是否下载
 * @returns {string} 导出地址
 */
function buildUrl(format, params, download) {
    const queryParams = Object.assign({}, params, {download: download ? 'true' : 'false'})
    const queryString = Object.keys(queryParams)
        .filter(key => queryParams[key] !== undefined && queryParams[key] !== null && queryParams[key] !== '')
        .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(queryParams[key])}`)
        .join('&')
    return `${EXPORT_BASE_URL}/${format}?${queryString}`
}

/**
 * 解析响应头中的文件名
 *
 * @param disposition Content-Disposition
 * @returns {string} 文件名
 */
function parseFileName(disposition) {
    if (!disposition) {
        return ''
    }
    const utf8Match = /filename\*=UTF-8''([^;]+)/i.exec(disposition)
    if (utf8Match) {
        try {
            return decodeURIComponent(utf8Match[1].replace(/^"|"$/g, ''))
        } catch (e) {
            return utf8Match[1]
        }
    }
    const normalMatch = /filename="?([^";]+)"?/i.exec(disposition)
    return normalMatch ? normalMatch[1] : ''
}

/**
 * 触发浏览器下载
 *
 * @param blob 文件内容
 * @param fileName 文件名称
 */
function triggerDownload(blob, fileName) {
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = fileName
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    // 延迟释放，避免部分浏览器下载被中断
    setTimeout(() => window.URL.revokeObjectURL(url), 1000)
}

/**
 * 下载导出文件
 *
 * @param url 导出地址
 * @param fallbackName 兜底文件名
 * @returns {Promise<void>}
 */
async function downloadFile(url, fallbackName) {
    const response = await fetch(url)
    const disposition = response.headers.get('Content-Disposition') || ''
    if (!disposition) {
        let message = '导出失败'
        try {
            const result = await response.json()
            if (result && result.msg) {
                message = result.msg
            }
        } catch (e) {
            // 响应不是JSON时使用默认提示
        }
        throw new Error(message)
    }
    const blob = await response.blob()
    triggerDownload(blob, parseFileName(disposition) || fallbackName)
}

/**
 * 新标签预览OpenAPI文档
 *
 * @param params 导出参数
 * @returns {boolean} 是否成功打开新标签
 */
export function previewOpenApi(params) {
    const win = window.open(buildUrl('openapi', params, false), '_blank')
    if (win) {
        win.opener = null
    }
    return !!win
}

/**
 * 下载OpenAPI JSON文件
 *
 * @param params 导出参数
 * @returns {Promise<void>}
 */
export function downloadOpenApi(params) {
    return downloadFile(buildUrl('openapi', params, true), 'openapi.json')
}

/**
 * 下载Markdown文件
 *
 * @param params 导出参数
 * @returns {Promise<void>}
 */
export function downloadMarkdown(params) {
    return downloadFile(buildUrl('markdown', params, true), 'markdown.md')
}

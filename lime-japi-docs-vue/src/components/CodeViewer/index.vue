<template>
    <div class="editor-container">
        <div class="editor-language">{{language}}</div>

        <copy-text :value="value" class="editor-copy">
            <a-icon type="copy"/>
        </copy-text>

        <editor
                ref="myEditor"
                @init="editorInit"
                :value="value"
                :lang="language"
                theme="chrome"
                :showLineNumbers="false"
                :height="boxHeight"
                :options="boxOptions"
        ></editor>

    </div>
</template>

<script>
    // const themeJs = require.context('brace/theme/', false, /\.js$/)
    import Editor from "vue2-ace-editor";
    // 主题
    import "brace/theme/chrome";
    // 代码片段
    import "brace/snippets/javascript";
    import "brace/snippets/dockerfile";
    import "brace/snippets/python";
    import "brace/snippets/php";
    import "brace/snippets/html";
    import "brace/snippets/css";
    import "brace/snippets/ruby";
    import "brace/snippets/java";
    import "brace/snippets/json";
    // 代码语言
    import "brace/mode/javascript";
    import "brace/mode/dockerfile";
    import "brace/mode/python";
    import "brace/mode/php";
    import "brace/mode/html";
    import "brace/mode/ruby";
    import "brace/mode/css";
    import "brace/mode/java";
    import "brace/mode/json";
    // 语言扩展
    import "brace/ext/language_tools";
    import "brace/ext/emmet";
    import "brace/ext/beautify";
    import "brace/ext/searchbox";
    import "brace/ext/chromevox";
    import "brace/ext/error_marker";
    import "brace/ext/keybinding_menu";
    import "brace/ext/linking";
    import "brace/ext/modelist";
    import "brace/ext/old_ie";
    import "brace/ext/settings_menu";
    import "brace/ext/spellcheck";
    import "brace/ext/split";
    import "brace/ext/static_highlight";
    import "brace/ext/statusbar";
    import "brace/ext/whitespace";
    import "brace/ext/textarea";
    import "brace/ext/themelist";
    export default {
        components: {
            Editor
        },
        props: {
            value: {
                type: String,
                default() { return "" }
            },
            language: {
                type: String,
                default() { return "js" }
            },
            height: {
                type: Number,
                default() { return 0 }
            },
            options: {
                type: Object,
                default() { return {}}
            }
        },
        data() {
            return {
                boxHeight: 0,
                boxOptions: {
                    enableBasicAutocompletion: true, // 启用基本自动完成
                    enableSnippets: true, // 启用代码段
                    enableLiveAutocompletion: true, // 启用实时自动完成
                    displayIndentGuides: false, // 显示参考线
                    enableEmmet: false, // 启用Emmet
                    tabSize: 2, // 标签大小
                    fontSize: 14, // 设置字号
                    useWorker: false, // 使用辅助对象
                    showPrintMargin: false, //去除编辑器里的竖线
                    readOnly: false, // 是否可读
                    showFoldWidgets: true, // 显示折叠部件
                    fadeFoldWidgets: true, // 淡入折叠部件
                    wrap: false, // 代码超过显示后是否换行
                    highlightActiveLine: false, // 是否点亮光标所在行
                    showLineNumbers: true, // 是否显示行号
                    showGutter: false, // 是否显示行号区域
                    behavioursEnabled: true, // 是否开启代码编辑行为（智能写代码）
                    cursorStyle: "slim" // 光标样式
                },
                copyStatus: false,
                copyTitle: "复制成功"
            };
        },
        created() {

            Object.assign(this.boxOptions, this.options)
            if (this.height) {
                this.boxHeight = this.height
            }

            // for (const item of themeJs.keys()) {
            //     const jsName = item.substring(2)
            //     require("brace/theme/" + jsName)
            //     const themeName = jsName.substring(0, jsName.length - 3)
            //     console.log(themeName)
            //     this.themeList.push(themeName)
            // }

        },
        methods: {
            editorInit(editor) {
                // 监听编辑器变化
                editor.getSession().on("change", () => {
                    this.$emit("change", editor.getValue())
                    if (!this.height) {
                        this.boxHeight = editor.getSession().getLength() * 25
                    } else {
                        this.boxHeight = this.height
                    }
                })
            },
        }
    };
</script>

<style lang="less" scoped>
    .editor-container {
        position: relative;
        width: 100%;
        padding: 30px 20px 20px 20px;
        background: #f8f8f8;
        .editor-language {
            color: #ccc;
            font-size: .6rem;
            font-weight: 600;
            height: 15px;
            line-height: 15px;
            padding: 5px 10px 0;
            position: absolute;
            left: 0;
            text-align: left;
            top: 0;
            user-select: none;
         }
        .editor-copy {
            color: #999;
            font-size: .8rem;
            font-weight: 600;
            line-height: 15px;
            padding: 5px;
            margin: 5px;
            border-radius: 2px;
            position: absolute;
            right: 0;
            top: 0;
            user-select: none;
            transition:  all .2s;
            cursor: pointer;
        }
        .editor-copy:hover {
            background-color: #eee;
            color: #666;
        }
    }
    /deep/ .ace-chrome {
        background: transparent;
    }
    /deep/ .ace_editor {
        font-size: 14px;
        line-height: 25px;
    }

</style>

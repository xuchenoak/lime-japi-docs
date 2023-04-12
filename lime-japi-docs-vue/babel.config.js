module.exports = {
    presets: [
        ['@vue/cli-plugin-babel/preset'],
        [
            "@babel/preset-env",
            {
                targets: "> 0.25%, not dead",
                useBuiltIns: "usage",
                corejs:3
            }
        ]
    ],
    plugins: [
        // ["prismjs", {
        //     "languages": ["javascript", "css", "markup", "java", "json"],
        //     "plugins": ["line-numbers"],
        //     "theme": "twilight",
        //     "css": true
        // }]
    ]
}

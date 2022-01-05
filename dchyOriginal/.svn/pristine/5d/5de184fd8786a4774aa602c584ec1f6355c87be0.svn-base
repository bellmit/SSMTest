var path = require('path')
var HtmlWebpackPlugin = require('html-webpack-plugin')
var CopyWebpackPlugin = require('copy-webpack-plugin')
var { CleanWebpackPlugin } = require('clean-webpack-plugin')
const ExtractTextPlugin = require("extract-text-webpack-plugin");
const UglifyJsPlugin = require('uglifyjs-webpack-plugin');

module.exports = {
    entry: {
        'babel-polyfill': 'babel-polyfill', //es6转es5,兼容IE
        app: './src/main.js' //入口文件
    },
    output: {
        path: path.resolve(__dirname, './dist'), //打包文件的位置
        publicPath: '/msurveyplat-serviceol/',
        filename: 'js/[name].js',
        chunkFilename: 'js/[name].js'
    },
    module: {
        rules: [{
                test: /\.vue$/,
                loader: 'vue-loader',
            },
            {
                test: /\.js$/,
                loader: 'babel-loader',
                options: {
                    presets: ['env']
                },
                exclude: /node_modules/
            },
            { test: /\.ts$/, loader: "ts-loader" },
            {
                test: /\.(png|jpe?g|gif|svg)(\?.*)?$/,
                loader: 'url-loader'
            },
            {
                test: /\.(woff2?|eot|ttf|otf)(\?.*)?$/,
                loader: 'url-loader'
            },
            {
                test: /\.css$/,
                use: ExtractTextPlugin.extract({
                    fallback: "vue-style-loader",
                    use: ["css-loader"]
                })
            },
            {
                test: /\.less$/,
                use: ExtractTextPlugin.extract({
                    fallback: "vue-style-loader",
                    use: ["style-loader!css-loader!less-loader"]
                })
            },
        ]
    },
    resolve: {
        extensions: ['.js', '.vue', '.json', '.ts'], //引入文件可省略文件后缀
        alias: {
            '@': path.resolve('./src/'), //引入文件@/表示从src级目录开始
            'vue$': 'vue/dist/vue.esm.js',
            'img': path.resolve(__dirname, './src/assets/img/'),
            'static': path.resolve(__dirname, './static/'),
        },
    },
    devServer: {
        historyApiFallback: {
            rewrites: [
                { from: /.*/, to: path.posix.join('/', 'index.html') },
            ],
        },
        inline: true,
        port: 6062,
        contentBase: false,
        proxy: { //本地启动调测，连接后台
            '/msurveyplat-promanage': {
                target: 'http://192.168.50.40:8086',
                changeOrigin: true,
                pathRewrite: {
                    '^/msurveyplat-promanage': '/msurveyplat-promanage'
                }
            },
            '/msurveyplat-serviceol': {
                target: 'http://192.168.50.40:8087',
                changeOrigin: true,
                pathRewrite: {
                    '^/msurveyplat-serviceol': '/msurveyplat-serviceol'
                }
            },
            '/msurveyplat-portalol': {
                target: 'http://192.168.50.40:8089',
                changeOrigin: true,
                pathRewrite: {
                    '^/msurveyplat-portalol': '/msurveyplat-portalol'
                }
            }
        }
    },
    plugins: [ //打包配置
        new ExtractTextPlugin('styles.css'),
        new HtmlWebpackPlugin({
            inject: true,
            template: './index.html',
            filename: 'index.html'
        }),
        new CopyWebpackPlugin([
            { from: 'static', to: 'static' },
            { from: 'src/assets', to: 'assets' }
        ]),
        new CleanWebpackPlugin()
    ]
}
if (process.env.NODE_ENV === 'production') {
    module.exports.plugins = (module.exports.plugins || []).concat([
        new UglifyJsPlugin({
            uglifyOptions: {
              compress: {
                drop_debugger: true,
                drop_console: true,  //生产环境自动删除console
              },
              warnings: false,
            },
            sourceMap: false,
            parallel: true //使用多进程并行运行来提高构建速度。默认并发运行数：os.cpus().length - 1。
        }),
    ])
} 

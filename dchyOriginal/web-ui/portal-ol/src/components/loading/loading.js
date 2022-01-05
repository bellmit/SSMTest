// loading.js
import LoadingComponent from './loading.vue'
//自定义loading组件
let $vm

export default {
    install(Vue, options) {
        if (!$vm) {
            const LoadingPlugin = Vue.extend(LoadingComponent);

            $vm = new LoadingPlugin({
                el: document.createElement('div')
            });

            document.body.appendChild($vm.$el);
        }

        $vm.show = false;

        let loading = {
            show(text) {
                $vm.show = true;

                $vm.text = text;
            },
            close() {
                $vm.show = false;
            }
        };

        if (!Vue.$loading) {
            Vue.$loading = loading;
        }

        Vue.mixin({
            created() {
                this.$loading = Vue.$loading;
            }
        })
    }
}
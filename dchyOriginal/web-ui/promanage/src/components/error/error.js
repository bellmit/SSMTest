// error.js
import ErrorComponent from './error.vue'
//自定义error组件
let $vm

export default {
    install(Vue, options) {
        if (!$vm) {
            const ErrorPlugin = Vue.extend(ErrorComponent);

            $vm = new ErrorPlugin({
                el: document.createElement('div')
            });

            document.body.appendChild($vm.$el);
        }

        $vm.show = false;

        let error = {
            show(text) {
                $vm.show = true;
                if(text){
                    $vm.text = text;
                }
                setTimeout(() => {
                    $vm.show = false;
                },1000)
            },
            close() {
                $vm.show = false;
            }
        };

        if (!Vue.$error) {
            Vue.$error = error;
        }

        Vue.mixin({
            created() {
                this.$error = Vue.$error;
            }
        })
    }
}
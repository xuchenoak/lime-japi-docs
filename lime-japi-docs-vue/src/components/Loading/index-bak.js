import Vue from 'vue';
import Loading from './index.vue';
const Mask = Vue.extend(Loading);


const toggleLoading = (el, binding) => {
        Vue.nextTick(() => {
            // insertDom(el, el, binding);
            el.instance.visible = binding.value
            console.log(el.mask)
            el.appendChild(el.mask)
        })
    }

export default {
    install(Vue) {
        Vue.use(Loading),
        Vue.directive('loading', {
            bind: function (el, binding, vnode) {
                console.log("v-loading指令生效")
                console.log(el)
                console.log(binding)
                console.log(vnode)

                const loadingText = el.getAttribute('loading-text');
                const loadingIcon = el.getAttribute('loading-icon');

                const mask = new Mask({
                    el: document.createElement('div'),
                    data() {
                        return {
                            text: loadingText || '努力加载中……',
                            icon: loadingIcon || 'loading',
                        }
                    }
                })

                console.log(mask, "mask")

                el.instance = mask;
                el.mask = mask.$el;




                binding.value && toggleLoading(el, binding);

            },
            update: function(el, binding) {
                el.instance.setText(el.getAttribute('loading-text'));
                if (binding.oldValue !== binding.value) {
                    toggleLoading(el, binding);
                }
            },

            unbind: function(el, binding) {
                // if (el.domInserted) {
                //     el.mask &&
                //     el.mask.parentNode &&
                //     el.mask.parentNode.removeChild(el.mask);
                //     toggleLoading(el, { value: false, modifiers: binding.modifiers });
                // }
                el.instance && el.instance.$destroy();
            }
        })
    }
}

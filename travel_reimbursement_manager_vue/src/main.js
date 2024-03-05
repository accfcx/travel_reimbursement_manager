import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui';
import echarts from 'echarts';
import 'element-ui/lib/theme-chalk/index.css';
import axios from 'axios'
import VueBpmnModeler from "vue-bpmn-modeler"
import "bpmn-js/dist/assets/diagram-js.css"
import "bpmn-js/dist/assets/bpmn-font/css/bpmn.css"
import "bpmn-js/dist/assets/bpmn-font/css/bpmn-codes.css"
import "bpmn-js/dist/assets/bpmn-font/css/bpmn-embedded.css";
import 'bpmn-js-properties-panel/dist/assets/bpmn-js-properties-panel.css'
import x2js from 'x2js'


// 设置后台访问路径
axios.defaults.baseURL = "http://localhost:8086"

Vue.config.productionTip = false


// 使用ElementUI
Vue.use(ElementUI);
Vue.use(VueBpmnModeler);
// 挂载axios
Vue.prototype.$http = axios
Vue.prototype.$echarts = echarts
Vue.prototype.$x2js = new x2js()

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')

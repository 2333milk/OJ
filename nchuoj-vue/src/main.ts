
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ArcoVue from '@arco-design/web-vue';
import App from './App.vue';
import '@arco-design/web-vue/dist/arco.css';
import router from './router'
import '@/access'
import 'bytemd/dist/index.css'
import ArcoVueIcon from '@arco-design/web-vue/es/icon';
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
const app = createApp(App)
const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)
app.use(pinia)
app.use(router)
app.use(ArcoVueIcon);
app.use(ArcoVue);
app.mount('#app')

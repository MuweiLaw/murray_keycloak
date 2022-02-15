// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui'
import axios from 'axios'
import Keycloak from 'keycloak-js'

import 'element-ui/lib/theme-chalk/index.css'

Vue.prototype.$axios = axios

Vue.config.productionTip = false

Vue.use(ElementUI)
/* eslint-disable no-new */

// keycloak init options
const initOptions = {
  url: 'http://127.0.0.1:8080/auth/',
  realm: 'demo',
  clientId: 'auth_web',
  onLoad: 'login-required'
}

const keycloak = Keycloak(initOptions)

keycloak.init({ onLoad: initOptions.onLoad, promiseType: 'native' }).then((authenticated) => {
  if (!authenticated) {
    window.location.reload()
  } else {
    Vue.prototype.$keycloak = keycloak
    console.log('Authenticated')
  }
  new Vue({
    el: '#app',
    router,
    components: { App },
    template: '<App/>',
    render: h => h(App)
  }).$mount('#app')

  setInterval(() => {
    keycloak.updateToken(70).then((refreshed) => {
      if (refreshed) {
        console.log('Token refreshed')
      } else {
        console.log('Token not refreshed, valid for ' + Math.round(keycloak.tokenParsed.exp + keycloak.timeSkew - new Date().getTime() / 1000) + ' seconds')
      }
    }).catch(error => {
      console.log('Failed to refresh token', error)
    })
  }, 60000)
}).catch(error => {
  console.log('Authenticated Failed', error)
})

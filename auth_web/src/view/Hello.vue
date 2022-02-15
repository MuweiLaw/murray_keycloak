<template>
  <div class="hello">
    <el-button @click="getAdmin">调用admin</el-button>
    <h1>{{ msg }}</h1>
    <div>
      <p>
        current user: {{user}}
      </p>
      <p>
        roles: {{roles}}
      </p>
      <p>
        {{adminMsg}}
      </p>
      <p>
        {{customerMsg}}
      </p>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'HelloWorld',
  props: {
    msg: String
  },
  data () {
    return {
      user: '',
      roles: [],
      adminMsg: '',
      customerMsg: ''
    }
  },
  created () {
    this.user = this.$keycloak.idTokenParsed.preferred_username
    this.roles = this.$keycloak.realmAccess.roles

    this.getAdmin()
      .then(response => {
        this.adminMsg = response.data
      })
      .catch(error => {
        console.log(error)
      })

    // this.getCustomer()
    //   .then(response => {
    //     this.customerMsg = response.data
    //   })
    //   .catch(error => {
    //     console.log(error)
    //   })
  },
  methods: {
    getAdmin () {
      return axios({
        method: 'get',
        url: 'http://127.0.0.1:8082/admin',
        headers: {'Authorization': 'Bearer ' + this.$keycloak.token}
      })
    }
    // getCustomer () {
    //   return axios({
    //     method: 'get',
    //     url: 'http://127.0.0.1:8082/customer',
    //     headers: {'Authorization': 'Bearer ' + this.$keycloak.token}
    //   })
    // }
  }
}
</script>

<template>
  <div>
    <!--  调用auth后端项目  -->
    <el-row>
      <el-col :span="12">
        <el-button @click="getAuthAdmin">调用auth:/admin</el-button>
      </el-col>
      <el-col :span="12">
        <el-button @click="getAuthCustomer">调用auth:/customer</el-button>
      </el-col>
    </el-row>
    <br/>
    <!--  调用authSecurity后端项目  -->
    <el-row>
      <el-col :span="6">
        <el-button @click="getAuthSecurityAdmin">调用authSecurity:/admin</el-button>
      </el-col>
      <el-col :span="6">
        <el-button @click="getAuthSecurityAdminZero">调用authSecurity:/adminZero</el-button>
      </el-col>
      <el-col :span="6">
        <el-button @click="getAuthSecurityCustomer">调用authSecurity:/customer</el-button>
      </el-col>
      <el-col :span="6">
        <el-button @click="getAuthSecurityCustomerZero">调用authSecurity:/customer/Zero</el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'Hello',
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

    // this.getAdmin()
    //   .then(response => {
    //     this.adminMsg = response.data
    //   })
    //   .catch(error => {
    //     console.log(error)
    //   })
  },
  methods: {
    getAuthAdmin () {
      return axios({
        method: 'get',
        url: 'http://127.0.0.1:8889/admin',
        headers: {'Authorization': 'Bearer ' + this.$keycloak.token}
      })
    },
    getAuthSecurityAdmin () {
      return axios({
        method: 'get',
        url: 'http://127.0.0.1:8890/admin',
        headers: {'Authorization': 'Bearer ' + this.$keycloak.token}
      })
    },
    getAuthSecurityAdminZero () {
      return axios({
        method: 'get',
        url: 'http://127.0.0.1:8890/adminZero',
        headers: {'Authorization': 'Bearer ' + this.$keycloak.token}
      })
    },
    getAuthCustomer () {
      return axios({
        method: 'get',
        url: 'http://127.0.0.1:8889/customer',
        headers: {'Authorization': 'Bearer ' + this.$keycloak.token}
      })
    },
    getAuthSecurityCustomer () {
      return axios({
        method: 'get',
        url: 'http://127.0.0.1:8890/customer',
        headers: {'Authorization': 'Bearer ' + this.$keycloak.token}
      })
    },
    getAuthSecurityCustomerZero () {
      return axios({
        method: 'get',
        url: 'http://127.0.0.1:8890/customer/Zero',
        headers: {'Authorization': 'Bearer ' + this.$keycloak.token}
      })
    }
  }
}
</script>

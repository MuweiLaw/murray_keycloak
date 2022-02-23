#  一、特点

## 1、简介

keycloak 为现代应用和分布式服务提供了一套完整的认证授权管理解决方案。它是开源的，是一个独立的认证授权服务器

   - 独立的认证授权服务器，提供完整的认证解决方案
   - 基于OpenId-connect &SAML协议
   - 基本的登陆注册，以及登陆注册主题修改
   - 具有独立的数据库，用于储存用户认证授权凭据
   - 支持联合数据库存储，比如集成Ldap服务器，提供SPL扩展
   - 提供管理API，用于管理keycloak所有的认证授权对象

## 2、OIDC

掌握Keycloak就必须对 OpenID Connect（**OIDC**）协议进行了解。**OIDC** 是 **OAuth 2.0** 的一个扩展协议

资料参考：[https://felord.cn/about-oidc.html](https://felord.cn/about-oidc.html)


## 3、Demo地址

[https://gitee.com/murraylaw/murray_keycloak](https://gitee.com/murraylaw/murray_keycloak.git)

# 二、使用流程

   

   ```sequence
   participant 用户(浏览器)
   participant webServer
   participant Keycloak Server
   Note right of 用户(浏览器): 1.用户第一次访问，查询session没有token
   Note right of webServer: 2.Client将用户导向keycloak server
   
   Note right of 用户(浏览器): 3.重定向到Keycloak Server
     用户(浏览器)->Keycloak Server: 4.输入用户名密码，进行登陆
   Note left of Keycloak Server: 5.验证成功，重定向到用户(浏览器)
     Keycloak Server->用户(浏览器): 5.并且返回授权码和state
   Note right of 用户(浏览器): 6.重定向到webServer
     用户(浏览器)->webServer: 重定向 webServer
   Note right of webServer: 获取授权码
     webServer->Keycloak Server: 请求keycloak 
     
     
   
   
   
   
   
   ```

#  三、keycloak控制台配置

**版本为16.1.1**

压缩包解压后，双击 bin => standalone.bat  启动不要关闭窗口

访问[http://localhost:8080](http://localhost:8080) 并点击Administration Console进行登陆，第一次进入需要创建账号密码

## 1、建立Realm

![image-20220215181826793](.\img\image-20220215181826793.png)

![image-20220215182236775](.\img\image-20220215182236775.png)

## 2、建立客户端

![image-20220215182716268](.\img\image-20220215182716268.png)

### ①建立前端应用客户端 auth_web，Access Type选择public

记住客户端名称需要填写到前端项目main.js中

![image-20220215183453445](.\img\image-20220215183453445.png)

创建后内容

![image-20220215183744519](.\img\image-20220215183744519.png)

更改前端项目端口

![image-20220215183205615](.\img\image-20220215183205615.png)

### ②建立后端应用客户端

建立一个新的客户端：auth，Access Type选择bearer-only

![image-20220215184738451](.\img\image-20220215184738451.png)

![image-20220215185146610](.\img\image-20220215185146610.png)

点击第①布，生成第二步中的字符串，后端配置文件中的keycloak.credentials.secret填写第②步中生成的字符串

![image-20220215193622468](.\img\image-20220215193622468.png)

## 3、建立角色

建立2个角色：ROLE_ADMIN、ROLE_CUSTOMER

![image-20220215194224113](.\img\image-20220215194224113.png)

![image-20220215194300818](.\img\image-20220215194300818.png)

## 4、建立用户

建立2个用户：admin、customer

![image-20220215194440200](.\img\image-20220215194440200.png)![image-20220215194509443](.\img\image-20220215194509443.png)

设置用户密码

![image-20220215194904549](.\img\image-20220215194904549.png)

查看刚才新增的两个用户![image-20220215194732344](.\img\image-20220215194732344.png)

## 5、分配角色

admin用户分配角色ROLE_ADMIN，customer用户分配角色ROLE_CUSTOMER

![image-20220215195118901](.\img\image-20220215195118901.png)![image-20220215195225322](.\img\image-20220215195225322.png)

# 四、后端配置

## 1、pom依赖项
   ``` xml
   <dependency>
            <groupId>org.keycloak</groupId>
            <artifactId>keycloak-spring-boot-starter</artifactId>
            <version>16.1.1</version>
   </dependency> 
   
   ```
## 2、适配器BOM依赖项
   ``` xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.keycloak.bom</groupId>
      <artifactId>keycloak-adapter-bom</artifactId>
      <version>16.1.1</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>
   ```
## 3、yaml说明

```yaml
keycloak:
  realm: demo                                     #领域的名称， 这是必需的
  auth-server-url: http://127.0.0.1:8080/auth     #Keycloak服务器的基本URL。 所有其他Keycloak页面和REST服务端点都从此派生。 通常采用https：// 											   #host：port / auth的形式。 这是必需的
  
  resource: auth                                  #应用程序的client-id，每个应用程序都有一个用于标识该应用程序的client-id。 这是必需的。
  ssl-required: external                          #确保与Keycloak服务器之间的所有通信均通过HTTPS。 在生产中，这应该设置为全部。 这是可选的。 默认值												 #为外部，这意味着外部请求默认情况下需要HTTPS。 有效值为“全部”，“外部”和“无”。
  
  confidential-port: 8443                         #Keycloak服务器用于通过SSL / TLS进行安全连接的机密端口。 这是可选的。 默认值为8443。
  credentials:
    secret: ujzxGBlNpP5c1HhMLHYfUPf9DT8j6j3j      #上文添加客户端后Credentials Tab内对应的内容
  public-client: false                            #如果设置为true，则适配器不会将客户端的凭据发送到Keycloak。 这是可选的。 默认值为false。
  bearer-only: true                               #设置为true，表示此应用的Keycloak访问类型是bearer-only
  use-resource-role-mappings: false               #如果设置为true，则适配器将在令牌内部查找用户的应用程序级角色映射。 如果为false，它将查看用户角色											  #映射的领域级别。 这是可选的。 默认值为false
  
  cors: true                                      #设置为true表示容许跨域访问
  security-constraints:                           #主要是针对不一样的路径定义角色以达到权限管理的目的
    - authRoles:
        - ROLE_CUSTOMER                           #只容许拥有`ROLE_CUSTOMER`角色的用户才能访问
      securityCollections:
        - name: customer                          #只容许账号"customer"的用户才能访问
          patterns:
            - /customer                           #拦截的映射地址
    - authRoles:
        - ROLE_ADMIN
      securityCollections:
        - name: admin
          patterns:
            - /admin
```



## 4、User Storage SPI
   如果你不想所有的用户数据都存储在keycloak的数据库中，你想要用户的部分数据存储在你自己的数据库。Keycloak提供联合存储来解决这种情景，通过实现User Storage SPI接口，可以做到将部分用户数据存储到你自己的数据库中，而另一部分用户数据存储在keycloak的数据库中。

### ①UserStorageProvider简介

| SPI 接口                 | 实现功能                                                    |
| ------------------------ | ----------------------------------------------------------- |
| UserStorageProvider      | 自定义的StorageProvider必须要实现的接口                     |
| UserLookupProvider       | 实现后，可以根据userId/username从你自己的数据中查询用户数据 |
| UserRegistrationProvider | 实现后，可以往自己数据库中增加删除修改用户数据              |
| CredentialInputUpdater   | 实现后，可以更新密码                                        |
| CredentialInputValidator | 验证密码的逻辑                                              |
| UserQueryProvider        | 从自己数据库中查询用户                                      |

### ②pom依赖

```xml
<dependency>
         <groupId>org.keycloak</groupId>
         <artifactId>keycloak-core</artifactId>
         <version>${keycloak.version}</version>
         <scope>provided</scope>
     </dependency>
     <dependency>
         <groupId>org.keycloak</groupId>
         <artifactId>keycloak-server-spi</artifactId>
         <version>${keycloak.version}</version>
         <scope>provided</scope>
     </dependency>
     <dependency>
         <groupId>org.keycloak</groupId>
         <artifactId>keycloak-services</artifactId>
         <version>${keycloak.version}</version>
         <scope>provided</scope>
     </dependency>
```

## 5、Java Admin API

Keycloak提供 Rest API 用于管理keycloak几乎所有的用户认证授权数据对象，如创建用户、查询用户、创建角色、查询会话等的API.Keycloak Rest API；另外，Keycloak将这些Rest Api封装成了一个java库，你只要提供keycloak服务器连接信息，就可以直接调用java api 去操作keycloak数据对象了。

### ①核心代码

getInstance()方法直接操作对象即可

```java
package com.example;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

public class KeycloakHelper {
    private static Keycloak keycloak;

    public static Keycloak getInstance() {
        if (keycloak == null) {
            synchronized (KeycloakHelper.class) {
                keycloak = initialKeycloakClient();
            }
        }
        return keycloak;
    }

    private static Keycloak initialKeycloakClient() {
        return KeycloakBuilder.builder()
                .serverUrl("http://localhost:8080/auth")
                .realm("master")
                .username("admin")
                .password("admin")
                .clientId("admin-cli")
                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
                .build();
    }
}
```

### ②pom依赖

```xml
		<dependency>
            <groupId>org.keycloak</groupId>
            <artifactId>keycloak-admin-client</artifactId>
            <version>16.1.1</version>
        </dependency>
```

# 五、前端配置

## 1、核心代码

package.json引入依赖

```json
"dependencies": {
    "vue": "^2.6.11",
    "element-ui": "^2.15.5",
    "vue-router": "^3.2.0",
    "axios": "^0.19.2",
    "core-js": "^3.6.4",
    "keycloak-js": "^16.1.1"
  },
```

main.js

```js

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

```

vue组件

```html
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

    this.getAdmin()
      .then(response => {
        this.adminMsg = response.data
      })
      .catch(error => {
        console.log(error)
      })
  },
  methods: {
    getAdmin () {
      return axios({
        method: 'get',
        url: 'http://127.0.0.1:8889/admin',
        headers: {'Authorization': 'Bearer ' + this.$keycloak.token}
      })
    }
  }
}
</script>
```

## 2、启动项目

```sh
# 打包编译
cnpm install
# 启动开发环境
npm run dev
```

## 3、访问

访问[http://localhost:8888](http://localhost:8888)

因为没有登录，故跳转到keycloak的用户登录页

![image-20220215195621273](.\img\image-20220215195621273.png)

接着输入刚才步骤 **三 - 4** 中创建用户的账密

第一次登录会要求修改密码

![image-20220215200222832](.\img\image-20220215200222832.png)

登录成功![image-20220215200351188](.\img\image-20220215200351188.png)

![image-20220215200527279](.\img\image-20220215200527279.png)

大功告成

# 六、接入Spring Security Adapter

资料参考: [https://felord.cn/categories/spring-security/keycloak/](https://felord.cn/categories/spring-security/keycloak/)

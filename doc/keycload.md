
#  一、特点

keycload 为现代应用和分布式服务提供了一套完整的认证授权管理解决方案。它是开源的，是一个独立的认证授权服务器

   - 独立的认证授权服务器，提供完整的认证解决方案
   - 基于OpenId-connect &SAML协议
   - 基本的登陆注册，以及登陆注册主题修改
   - 具有独立的数据库，用于储存用户认证授权凭据
   - 支持联合数据库存储，比如集成Ldap服务器，提供SPL扩展
   - 提供管理API，用于管理keycloak所有的认证授权对象

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

   

# 三、后端配置
## pom依赖项
   ``` xml
   <dependency>
            <groupId>org.keycloak</groupId>
            <artifactId>keycloak-spring-boot-starter</artifactId>
            <version>16.1.1</version>
   </dependency> 
   
   ```
## 适配器BOM依赖项
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
## yaml

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



## User Storage SPI
   如果你不想所有的用户数据都存储在keycloak的数据库中，你想要用户的部分数据存储在你自己的数据库。Keycloak提供联合存储来解决这种情景，通过实现User Storage SPI接口，可以做到将部分用户数据存储到你自己的数据库中，而另一部分用户数据存储在keycloak的数据库中。

### UserStorageProvider简介

| SPI 接口                 | 实现功能                                                    |
| ------------------------ | ----------------------------------------------------------- |
| UserStorageProvider      | 自定义的StorageProvider必须要实现的接口                     |
| UserLookupProvider       | 实现后，可以根据userId/username从你自己的数据中查询用户数据 |
| UserRegistrationProvider | 实现后，可以往自己数据库中增加删除修改用户数据              |
| CredentialInputUpdater   | 实现后，可以更新密码                                        |
| CredentialInputValidator | 验证密码的逻辑                                              |
| UserQueryProvider        | 从自己数据库中查询用户                                      |

### pom依赖

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

# # 五、demo地址(包含前后端)

[https://gitee.com/murraylaw/murray_keycloak](https://gitee.com/murraylaw/murray_keycloak.git)
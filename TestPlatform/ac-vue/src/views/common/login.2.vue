<template>
  <div class="main">
    <div class="logindiv">
      <el-row>
        <el-col :span="12">
          <div><img src="~@/assets/img/2018wechat.jpg"/></div>
        </el-col>
        <el-col :span="12" class="loginform">
          <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" status-icon>
            <el-form-item prop="userName">
              <el-col :span="20">
              <el-input v-model="dataForm.userName" placeholder="帐号"></el-input>
              </el-col>
            </el-form-item>
            <el-form-item prop="password">
              <el-col :span="20">
              <el-input v-model="dataForm.password" type="password" placeholder="密码"></el-input>
              </el-col>
            </el-form-item>
            <el-form-item prop="captcha">
              <el-row :gutter="20">
                <el-col :span="10">
                  <el-input v-model="dataForm.captcha" placeholder="验证码">
                  </el-input>
                </el-col>
                <el-col :span="10" class="login-captcha">
                  <img :src="captchaPath" @click="getCaptcha()" alt="">
                </el-col>
              </el-row>
            </el-form-item>
            <el-form-item>
              <el-button class="login-btn-submit" type="primary" @click="dataFormSubmit()">登录</el-button>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import {
    getUUID,
    encrypt,
    decrypt
} from '@/utils'
export default {
    data() {
        return {
            dataForm: {
                userName: '',
                password: '',
                uuid: '',
                captcha: ''
            },
            dataRule: {
                userName: [{
                    required: true,
                    message: '帐号不能为空',
                    trigger: 'blur'
                }],
                password: [{
                    required: true,
                    message: '密码不能为空',
                    trigger: 'blur'
                }],
                captcha: [{
                    required: true,
                    message: '验证码不能为空',
                    trigger: 'blur'
                }]
            },
            captchaPath: ''
        }
    },
    created() {
        this.getCaptcha()
    },
    methods: {
        // 提交表单
        dataFormSubmit() {
            this.$refs['dataForm'].validate((valid) => {
                if (valid) {
                    this.$http({
                        url: this.$http.adornUrl('/sys/login'),
                        method: 'post',
                        data: this.$http.adornData({
                            'username': this.dataForm.userName,
                            'password': encrypt(this.dataForm.password, this.dataForm.uuid.replace(new RegExp('-', 'g'), "")),
                            'uuid': this.dataForm.uuid,
                            'captcha': this.dataForm.captcha
                        })
                    }).then(({
                        data
                    }) => {
                        if (data && data.code === 0) {
                            this.$cookie.set('token', data.token)
                            this.$router.replace({
                                name: 'home'
                            })
                        } else {
                            this.getCaptcha()
                            this.$message.error(data.msg)
                        }
                    })
                }
            })
        },
        // 获取验证码
        getCaptcha() {
            this.dataForm.uuid = getUUID()
            this.captchaPath = this.$http.adornUrl(`/captcha.jpg?uuid=${this.dataForm.uuid}`)
        }
    }
}
</script>

<style lang="scss">
html, body, .el-container {
    background-color: #f0f0f0;
    height: 100%;
}

.main {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;

}

.logindiv {
    width: 800px;
    height: 400px;
    // border: #FF1400 solid 1px;    
    background-color: rgba(254, 45, 83, 0.63)
}

.loginform {
    padding-left: 30px;
    margin-top: 80px;
}

.login-title {
        font-size: 16px;
    }

    .login-captcha {
        overflow: hidden;overflow: hidden;

        >img {
            width: 120px;
            cursor: pointer;
        }
    }

    .login-btn-submit {
        width: 100px;
        margin-top: 38px;
    }

// .site-wrapper.site-page--login {
//     position: absolute;
//     top: 0;
//     right: 0;
//     bottom: 0;
//     left: 0;
//     background-color: rgba(38, 50, 56, .6);
//     overflow: hidden;

//     &:before {
//         position: fixed;
//         top: 0;
//         left: 0;
//         z-index: -1;
//         width: 100%;
//         height: 100%;
//         content: "";
//         background-image: url(~@/assets/img/login_bg.jpg);
//         background-size: cover;
//     }

//     .site-content__wrapper {
//         position: absolute;
//         top: 0;
//         right: 0;
//         bottom: 0;
//         left: 0;
//         padding: 0;
//         margin: 0;
//         overflow-x: hidden;
//         overflow-y: auto;
//         background-color: transparent;
//     }

//     .site-content {
//         min-height: 100%;
//         padding: 30px 500px 30px 30px;
//     }

//     .brand-info {
//         margin: 220px 100px 0 90px;
//         color: #fff;
//     }

//     .brand-info__text {
//         margin: 0 0 22px 0;
//         font-size: 48px;
//         font-weight: 400;
//         text-transform: uppercase;
//     }

//     .brand-info__intro {
//         margin: 10px 0;
//         font-size: 16px;
//         line-height: 1.58;
//         opacity: .6;
//     }

//     .login-main {
//         position: absolute;
//         top: 0;
//         right: 0;
//         padding: 150px 60px 180px;
//         width: 470px;
//         min-height: 100%;
//         background-color: #fff;
//     }

    
// }



</style>

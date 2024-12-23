<template>
  <div style="height: 100vh;display: flex;align-items: center;justify-content: center;background-color:#40a9ff">
    <div style="display: flex;background-color: white;width: 40%;height:42%;border-radius: 26px;overflow: hidden">
      <div style="flex: 1">
        <img height="400" src="../assets/登录3.gif" width="300"/>
      </div>
      <div style="flex: 1;display: flex;align-items: center;justify-content: center">
        <el-form ref="loginRef" :model="user" :rules="rules" style="width: 90%">
          <div style="font-size: 23px;font-weight: bold;text-align: center;margin-bottom: 20px">欢迎登录</div>
          <el-form-item prop="username">
            <el-input v-model="user.username" placeholder="请输入账号" prefix-icon="el-icon-user"
                      size="medium"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="user.password" placeholder="请输入密码" prefix-icon="el-icon-lock" show-password
                      size="medium"></el-input>
          </el-form-item>
          <el-form-item prop="code">
            <div style="display: flex">
              <el-input v-model="user.code" placeholder="请输入验证码" prefix-icon="el-icon-circle-check" size="medium"
                        style="flex: 1"></el-input>
              <div style="flex: 1;height: 36px">
                <valid-code @update:changeCode="getCode"/>
              </div>
            </div>
          </el-form-item>
          <el-form-item>
            <el-button style="width: 100%" type="primary" @click="login">登 录</el-button>
          </el-form-item>
          <div style="display: flex">
            <div style="flex: 1">还没有账号？请 <span style="color: #0f9876;cursor: pointer"
                                                     @click="$router.push('/register')">注册</span></div>
            <div style="flex: 1;text-align: right"><span style="color: #0f9876; cursor:pointer"
                                                         @click="handleForgetPass">忘记密码</span></div>
          </div>
        </el-form>
      </div>
    </div>

    <el-dialog :visible.sync="forgetPassDialogVis" style="text-align: center" title="忘记密码" width="30%">
      <el-form ref="forgetRef" :model="forgetUserForm" :rules="rules2">
        <el-form-item label="用户名" label-width="80px" prop="username" style="padding-right: 20px">
          <el-input v-model="forgetUserForm.username" autocomplete="off" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="手机号" label-width="80px" prop="phone" style="padding-right: 20px">
          <el-input v-model="forgetUserForm.phone" autocomplete="off" placeholder="请输入手机号(必填)"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" label-width="80px" prop="email" style="padding-right: 20px">
          <el-input v-model="forgetUserForm.email" autocomplete="off" placeholder="请输入邮箱(必填)"></el-input>
        </el-form-item>
      </el-form>
      <div style="text-align: center;color: red">手机号与邮箱有一个正确即可</div>
      <div style="text-align: center;color: red">默认密码为123</div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="forgetPassDialogVis = false">取 消</el-button>
        <el-button type="primary" @click="resetPassword">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>

import ValidCode from "@/conponents/ValidCode.vue";

export default {
  name: 'Login',
  // computed: {
  //   home() {
  //     return home
  //   },
  //   Manager() {
  //     return Manager
  //   }
  // },
  components: {
    ValidCode
  },
  data() {
    //验证码校验
    const validateCode = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入验证码'))
      } else if (value.toLowerCase() !== this.code) {
        callback(new Error('验证码错误'))
      } else {
        callback()
      }
    };

    return {
      forgetUserForm: {
        username: '',
        phone: '',
        email: ''
      }, rules2: {
        username: [
          {required: true, message: '请输入账号', trigger: 'blur'},
        ],
        phone: [
          {required: true, message: '请输入电话', trigger: 'blur'},
        ],
        email: [
          {required: true, message: '请输入邮箱', trigger: 'blur'},
        ],
      },
      forgetPassDialogVis: false,
      code: '',//验证码组件传过来的code
      user: {
        code: '',//表单里用户输入的验证码
        username: '',
        password: ''
      },
      rules: {
        username: [
          {required: true, message: '请输入账号', trigger: 'blur'},
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
        ],
        code: [
          {validator: validateCode, trigger: 'blur'}
        ],
      }
    }
  },
  created() {
  },
  methods: {
    handleForgetPass() {  //初始化表单的数据
      this.forgetUserForm = {}
      this.forgetPassDialogVis = true
    },
    resetPassword() {
      this.$refs['forgetRef'].validate((valid) => {
        if (valid) {
          this.$request.put('/password', this.forgetUserForm).then(res => {
            if (res.code === '200') {
              this.$message.success('重置成功')
              this.forgetPassDialogVis = false //重置密码后关闭弹窗
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    },
    getCode(code) {
      this.code = code.toLowerCase()//与第82行对应起来了
    },
    login() {
      this.$refs['loginRef'].validate((valid) => {
        if (valid) {
          //验证通过
          this.$request.post('/login', this.user).then(res => {
            if (res.code === '200') {
              if (res.data.role === '用户') {
                this.$router.push('/front/home')
              } else {
                this.$router.push('/')
              }
              this.$message.success('登录成功')
              localStorage.setItem("honey-user", JSON.stringify(res.data))  //存储用户数据
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    },
    // 监听回车键执行事件
    keyDown(e) {
      // 回车则执行登录方法 enter键的ASCII是13
      if (e.keyCode === 13) {
        this.login() // 需要执行的方法方法
      }
    }
  },
  // 绑定监听事件
  mounted() {
    window.addEventListener('keydown', this.keyDown)
  },
  // 销毁事件
  destroyed() {
    window.removeEventListener('keydown', this.keyDown, false)
  }
}
</script>

<style scoped>

</style>
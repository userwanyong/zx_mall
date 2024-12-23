<template>
  <div style="height: 100vh;display: flex;align-items: center;justify-content: center;background-color: dodgerblue">
    <div style="display: flex;background-color: white;width: 40%;height:42%;border-radius: 26px;overflow: hidden">
      <div style="flex: 1">


        <img height="400" src="../assets/注册.gif" width="350"/>
      </div>
      <div style="flex: 1;display: flex;align-items: center;justify-content: center">
        <el-form ref="registerRef" :model="user" :rules="rules" style="width: 90%">
          <div style="font-size: 23px;font-weight: bold;text-align: center;margin-bottom: 20px">欢迎注册</div>
          <el-form-item prop="username">
            <el-input v-model="user.username" placeholder="请输入账号" prefix-icon="el-icon-user"
                      size="medium"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="user.password" placeholder="请输入密码" prefix-icon="el-icon-lock" show-password
                      size="medium"></el-input>
          </el-form-item>
          <el-form-item prop="confirmPass">
            <el-input v-model="user.confirmPass" placeholder="请确认密码" prefix-icon="el-icon-lock" show-password
                      size="medium"></el-input>
          </el-form-item>
          <el-form-item prop="role">
            <el-radio-group v-model="user.role">
              <el-radio label="用户"></el-radio>
              <el-radio label="商家"></el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item>
            <el-button style="width: 100%" type="success" @click="register">注 册</el-button>
          </el-form-item>
          <div style="display: flex">
            <div style="flex: 1">以有账号？请 <span style="color: #6e77f2;cursor: pointer"
                                                   @click="$router.push('/login')">登录</span></div>

          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>


export default {
  name: 'Register',
  data() {
    //确认密码
    const validatePassword = (rule, confirmPass, callback) => {
      if (confirmPass === '') {
        callback(new Error('请确认密码'))
      } else if (confirmPass !== this.user.password) {
        callback(new Error('两次输入密码不一致'))
      } else {
        callback()
      }
    }
    return {

      user: {

        username: '',
        password: '',
        confirmPass: ''
      }, rules: {
        username: [
          {required: true, message: '请输入账号', trigger: 'blur'},
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
        ],
        confirmPass: [
          {validator: validatePassword, trigger: 'blur'}
        ],
        role: [
          {required: true, message: '选择角色', trigger: 'blur'},
        ]
      }
    }
  },
  created() {
  },
  methods: {

    register() {
      this.$refs['registerRef'].validate((valid) => {
        if (valid) {
          //验证通过
          this.$request.post('http://localhost:8080/register', this.user).then(res => {
            if (res.code === '200') {
              this.$router.push('/login')
              this.$message.success('注册成功')
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
        this.register() // 需要执行的方法方法
      }
    },

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
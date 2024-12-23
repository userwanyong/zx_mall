<template>
  <div class="card-container">
    <el-card style="width: 50%">
      <div style="text-align:center;font-size: 25px;padding-bottom: 25px">修改密码</div>
      <el-form ref="formRef" :model="user" :rules="rules" label-width="80px" style="padding-right: 20px">
        <el-form-item label="原始密码" prop="oldPassword">
          <el-input v-model="user.oldPassword" placeholder="原始密码" show-password></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="user.newPassword" placeholder="新密码" show-password></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="user.confirmPassword" placeholder="确认密码" show-password></el-input>
        </el-form-item>
        <div style="text-align: center">
          <el-button size="medium" type="primary" @click="update">确认修改</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "Password",
  data() {
    const validatePassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请确认密码'))
      } else if (value !== this.user.newPassword) {
        callback(new Error('两次密码不一致'))
      } else {
        callback()
      }
    };

    return {
      user: JSON.parse(localStorage.getItem('honey-user') || '{}'),
      rules: {
        oldPassword: [
          {required: true, message: '请输入原始密码', trigger: 'blur'},
        ],
        newPassword: [
          {required: true, message: '请输入新密码', trigger: 'blur'},
        ],
        confirmPassword: [
          {validator: validatePassword, required: true, trigger: 'blur'},
        ],
      }
    }
  },
  created() {

  },
  methods: {
    update() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.user.password = this.user.newPassword
          //保存当前用户信息到数据库
          this.$request.put('/user/updatePassword', this.user).then(res => {
            if (res.code === '200') {
              //成功更新
              this.$message.success('保存成功')
              this.$router.push('/login')
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    },
  }
}
</script>

<style scoped>
/deep/ .el-form-item__label {
  font-weight: bold;
}

.card-container {
  display: flex;
  justify-content: center;
  align-items: center;
}

</style>

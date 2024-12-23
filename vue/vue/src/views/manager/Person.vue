<template>
  <div>
    <el-card style="width: 50%">
      <el-form :model="user" label-width="80px" style="padding-right: 20px">
        <div style="margin: 15px;text-align: center">
          <el-upload
              :headers="{ token: user.token }"
              :on-success="handleAvatarSuccess"
              :show-file-list="false"
              action="http://localhost:8080/file/upload"
              class="avatar-uploader"
          >
            <img v-if="user.avatar" :src="user.avatar" alt="" class="avatar"/>
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </div>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="user.username" :disabled="true" placeholder="用户名"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="user.name" placeholder="姓名"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="role">
          <el-radio-group v-model="user.gender">
            <el-radio label="男"></el-radio>
            <el-radio label="女"></el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="user.phone" placeholder="电话"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="user.email" placeholder="邮箱"></el-input>
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="user.address" placeholder="地址" type="textarea"></el-input>
        </el-form-item>
        <div style="text-align: center">
          <el-button type="primary" @click="update">保 存</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "Person",
  data() {
    return {
      user: JSON.parse(localStorage.getItem('honey-user') || '{}')
    }
  },
  created() {

  },
  methods: {
    update() {
      //保存当前用户信息到数据库
      this.$request.put('/user/update', this.user).then(res => {
        if (res.code === '200') {
          //成功更新
          this.$message.success('保存成功')

          //更新浏览器缓存里的用户信息
          localStorage.setItem('honey-user', JSON.stringify(this.user))

          //触发父级的数据更新
          this.$emit('update:user', this.user)

        } else {
          this.$message.error(res.msg)
        }
      })

    },
    handleAvatarSuccess(response, file, fileList) {
      //把user的头像换成上传的图片的链接
      this.user.avatar = response.data
    },

  }
}


</script>

<style scoped>
/deep/ .el-form-item__label {
  font-weight: bold;
}

/deep/ .el-upload {
  border-radius: 50%;
}

/deep/ .avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  border-radius: 50%;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
  border-radius: 50%;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
  border-radius: 50%;
}

</style>

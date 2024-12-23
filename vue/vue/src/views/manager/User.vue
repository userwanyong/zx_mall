<template>
  <div>
    <div>
      <el-input v-model="username" placeholder="查询用户名" style="width: 200px"></el-input>
      <el-input v-model="name" placeholder="查询姓名" style="width: 200px;margin: 0 5px"></el-input>
      <el-button icon="el-icon-search" type="primary" @click="load(1)">查询</el-button>
      <el-button icon="el-icon-refresh" type="primary" @click="reset">重置</el-button>

      <el-button plain style="margin-left: 578px" type="primary" @click="handleAdd">新增</el-button>
      <el-button plain type="danger" @click="delBatch">批量删除</el-button>
      <el-button plain type="success" @click="exportData">批量导出</el-button>
      <el-upload :headers="{token: user.token}" :on-success="handleImport" :show-file-list="false"
                 action="http://localhost:8080/user/import" style="display: inline-block;margin-left: 10px">
        <el-button plain type="success">批量导入</el-button>
      </el-upload>
    </div>
    <div style="margin: 10px 0"></div>

    <el-table :data="tableData" :header-cell-style="{backgroundColor: 'aliceblue',color: '#666'}" stripe
              @selection-change="handleSelectionChange">>
      <el-table-column align="center" type="selection" width="55"></el-table-column>
      <el-table-column label="用户名" prop="username"></el-table-column>
      <el-table-column label="姓名" prop="name"></el-table-column>
      <el-table-column label="姓别" prop="gender"></el-table-column>
      <el-table-column label="手机号" prop="phone" width="200"></el-table-column>
      <el-table-column label="邮箱" prop="email" width="200"></el-table-column>
      <el-table-column label="地址" prop="address" width="200"></el-table-column>
      <el-table-column label="头像" width="160">
        <template v-slot="scope">
          <div style="display: flex;align-items: center;width: 57px;height: 57px">
            <el-image v-if="scope.row.avatar" :preview-src-list="[scope.row.avatar]"
                      :src="scope.row.avatar" style="width: 57px;height: 57px;border-radius: 50%"></el-image>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="角色" prop="role"></el-table-column>
      <el-table-column align="center" label="操作" width="150">
        <template v-slot="scope">
          <el-button plain size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button plain size="mini" type="danger" @click="del(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div style="margin: 10px 0">
      <el-pagination
          :current-page="pageNum"
          :page-size="pageSize"
          :page-sizes="[100, 200, 300, 400]"
          :total="total"
          layout="total, prev, pager, next"
          @current-change="handleCurrentChange">
      </el-pagination>
    </div>
    <el-dialog :visible.sync="fromVisible" title="编辑" width="30%">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px" style="padding-right: 20px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="用户名"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="姓名"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="role">
          <el-radio-group v-model="form.gender">
            <el-radio label="男"></el-radio>
            <el-radio label="女"></el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="form.phone" placeholder="手机号"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="邮箱"></el-input>
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address" placeholder="地址" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-radio-group v-model="form.role">
            <el-radio label="管理员"></el-radio>
            <el-radio label="用户"></el-radio>
            <el-radio label="商家"></el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="头像">
          <el-upload
              :headers="{ token: user.token }"
              :on-success="handleAvatarSuccess"
              :show-file-list="true"
              action="http://localhost:8080/file/upload"
              class="avatar-uploader"
              list-type="picture"
          >
            <el-button type="primary">上传头像</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>


  </div>
</template>


<script>
export default {
  name: "User",
  data() {
    return {
      tableData: [],  //所有的数据
      pageNum: 1,  //当前的页码
      pageSize: 9,  // 每页现实的个数
      username: '',
      name: '',
      total: 0,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('honey-user') || '{}'),
      rules: {
        username: [
          {required: true, message: '请输入账号', trigger: 'blur'}
        ]
      },
      ids: [],
      records: ''
    }
  },
  created() {
    this.load()
  },
  methods: {
    handleImport(res, file, fileList) {
      if (res.code === '200') {
        this.$message.success("导入成功")
        this.load(1)
      } else {
        this.$message.error(res.msg)
      }
    },
    exportData() {  //批量导出数据
      if (!this.ids.length) {
        window.open('http://localhost:8080/user/export?token=' + this.user.token + "&username=" + this.username + "&name=" + this.name)
      } else {
        let idStr = this.ids.join(',')
        window.open('http://localhost:8080/user/export?token=' + this.user.token + '&ids=' + idStr)
      }
    },
    delBatch() {
      if (!this.ids.length) {
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('您确认批量删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/user/delete/batch', {data: this.ids}).then(res => {
          if (res.code === '200') {//操作成功
            this.$message.success('操作成功')
            this.load(1)//刷新表格数据
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {
      })
    },
    handleSelectionChange(rows) {
      this.ids = rows.map(v => v.id)
    },
    del(id) {
      this.$confirm('您确认删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/user/delete/' + id).then(res => {
          if (res.code === '200') {//操作成功
            this.$message.success('删除成功')
            this.load(1)//刷新表格数据
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {
      })
    },
    handleEdit(row) {//编辑数据
      this.form = JSON.parse(JSON.stringify(row))//给form对象赋值
      this.fromVisible = true//打开弹窗
    },
    handleAdd() {//新增数据
      this.form = {role: '用户'}  //默认是用户
      this.fromVisible = true  //打开弹窗
    },
    save() { //保存按钮触发的逻辑 新增或更新
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request({
            url: this.form.id ? '/user/update' : '/user/add',
            method: this.form.id ? 'PUT' : 'POST',//根据id判断是新增还是编辑
            data: this.form
          }).then(res => {
            if (res.code === '200') {
              this.$message.success('修改成功')
              this.load(1)
              this.fromVisible = false//关闭弹窗
              //更新浏览器缓存里的用户信息
              localStorage.setItem('honey-user', JSON.stringify(this.user))
              //触发父级的数据更新
              this.$emit('update:user', this.user)
            } else {
              this.$message.error("用户名已存在")
            }
          })
        }
      })
    },
    reset() {
      this.name = ''
      this.username = ''
      this.load()
    },
    load(pageNum) {//分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/user/selectByPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          username: this.username,
          name: this.name
        }
      }).then(res => {
        if (res && res.data && res.data.records) {
          this.tableData = res.data.records
        }
        this.total = res.data.total
      })
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    handleAvatarSuccess(response, file, fileList) {
      //把user的头像换成上传的图片的链接
      this.form.avatar = response.data
      this.user.avatar = response.data
    },
  }
}


</script>

<style scoped>


</style>

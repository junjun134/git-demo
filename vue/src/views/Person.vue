<template>
  <el-card style="width: 500px;font-size: 15px;margin:0 30%">
    <el-form ref="from" :model="form" :rules="rules" label-width="80px" size="15px">
      <el-upload
          class="avatar-uploader"
          :action="'http://' + serverIp +':9090/file/upload'"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
      >
        <img v-if="form.avatarUrl" :src="form.avatarUrl" class="avatar">
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
      </el-upload>

      <el-form-item prop="username"  label="用户名">
        <el-input v-model="form.username" disabled autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item v-model="form.gender"  label="性别">
        <span>{{form.gender===1?"女":"男"}}</span>
      </el-form-item>
      <el-form-item prop="nickname" label="姓名">
        <el-input v-model="form.nickname"></el-input>
      </el-form-item>
      <el-form-item label="部门:">
        <span>{{form.deptName}}</span>
      </el-form-item>
      <el-form-item label="角色:">
        <span>{{form.roleName}}</span>
      </el-form-item>
      <el-form-item label="入职时间">
        <div class="block">
          <span class="demonstration"></span>
          <el-date-picker
              v-model="form.joinTime"
              type="date"
              placeholder="选择日期">
          </el-date-picker>
        </div>
      </el-form-item>
      <el-form-item prop="email" label="邮箱">
        <el-input v-model="form.email" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item prop="phone" label="电话">
        <el-input v-model="form.phone" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="地址">
        <el-input v-model="form.address" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="薪资情况" size="small">
        <el-descriptions direction="vertical" :column="4" border>
          <el-descriptions-item label="实发薪资" :span="4">{{Salary.endSalary}}</el-descriptions-item>
          <el-descriptions-item label="基本薪资" :span="3">{{Salary.baseSalary}}</el-descriptions-item>
          <el-descriptions-item label="奖金">{{ Salary.bonus }}</el-descriptions-item>
          <el-descriptions-item label="保险" :span="2">{{ Salary.insure }}</el-descriptions-item>
          <el-descriptions-item label="罚金">{{ Salary.fine }}</el-descriptions-item>
        </el-descriptions>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="sava1">确 定</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
import {serverIp} from "../../public/config";

export default {
  name: "Person",
  data() {
    return {
      serverIp: serverIp,
      form: {},
      depts:[],
      Salary:{},
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      rules: {
        nickname: [
          {required: true, message: '请输入正确姓名', trigger: 'blur'},
          {min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur'},
        ],
        email: [
          {required: true, message: '邮箱不能为空', trigger: 'blur'},
          {pattern: /^([a-zA-Z0-9]+[-_\.]?)+@[a-zA-Z0-9]+\.[a-z]+$/, message: "请输入正确的邮箱", trigger: "blur"}
        ],
        phone: [
          {required: true, message: "手机号不能为空",trigger: "blur"},
          {pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输入正确的手机号码", trigger: "blur"}
        ]

      }
    }

  },
  created() {
    this.getUser().then(res => {
      console.log(res)
      this.form = res
    })
    this.getSalary().then(res => {
      console.log(res)
      this.Salary = res
    })
    this.request.get("/role").then(res => {
      this.roles = res.data
    })
    this.request.get("/dept").then(res => {
      this.depts = res.data
    })
  },
  methods: {
    async getUser() {
      return (await this.request.get("/user/username/" + this.user.username)).data
    },
    async getSalary() {
      return (await this.request.get("/salary/username/" + this.user.username)).data
    },

    sava1() {
      this.$refs.from.validate((valid) => {
        if(!valid) {
          this.$message.warning('请调整数据后再提交')
        }else {
          this.request.post("/user", this.form).then(res => {
            if (res.code === '200') {
              this.$message.success("保存成功")

              // 触发父级更新User的方法
              this.$emit("refreshUser")

              // 更新浏览器存储的用户信息
              this.getUser().then(res => {
                res.token = JSON.parse(localStorage.getItem("user")).token
                localStorage.setItem("user", JSON.stringify(res))
              })
            } else {
              this.$message.error("保存失败")
            }
          })
        }
      })
    },

    save() {
      // this.request.post("/user", this.form).then(res => {
      this.request.post("/user", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")

          // 触发父级更新User的方法
          this.$emit("refreshUser")

          // 更新浏览器存储的用户信息
          this.getUser().then(res => {
            res.token = JSON.parse(localStorage.getItem("user")).token
            localStorage.setItem("user", JSON.stringify(res))
          })
        } else {
          this.$message.error("保存失败")
        }
      })
    },
    handleAvatarSuccess(res) {
      this.form.avatarUrl = res
    }
  }
}
</script>

<style>
.avatar-uploader {
  text-align: center;
  padding-bottom: 10px;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 138px;
  height: 138px;
  line-height: 138px;
  text-align: center;
}
.avatar {
  width: 138px;
  height: 138px;
  display: block;
}
</style>


<!--<template>-->
<!--  <el-card style="width: 500px;">-->
<!--    <el-form label-width="80px" size="small">-->
<!--      <el-upload-->
<!--          class="avatar-uploader"-->
<!--          :action="'http://' + serverIp +':9090/file/upload'"-->
<!--          :show-file-list="false"-->
<!--          :on-success="handleAvatarSuccess"-->
<!--      >-->
<!--        <img v-if="form.avatarUrl" :src="form.avatarUrl" class="avatar">-->
<!--        <i v-else class="el-icon-plus avatar-uploader-icon"></i>-->
<!--      </el-upload>-->

<!--      <el-form-item label="用户名">-->
<!--        <el-input v-model="form.username" disabled autocomplete="off"></el-input>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="昵称">-->
<!--        <el-input v-model="form.nickname" autocomplete="off"></el-input>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="邮箱">-->
<!--        <el-input v-model="form.email" autocomplete="off"></el-input>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="电话">-->
<!--        <el-input v-model="form.phone" autocomplete="off"></el-input>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="地址">-->
<!--        <el-input type="textarea" v-model="form.address" autocomplete="off"></el-input>-->
<!--      </el-form-item>-->
<!--      <el-form-item>-->
<!--        <el-button type="primary" @click="save">确 定</el-button>-->
<!--      </el-form-item>-->
<!--    </el-form>-->
<!--  </el-card>-->
<!--</template>-->

<!--<script>-->
<!--import {serverIp} from "../../public/config";-->

<!--export default {-->
<!--  name: "Person",-->
<!--  data() {-->
<!--    return {-->
<!--      serverIp: serverIp,-->
<!--      form: {},-->
<!--      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}-->
<!--    }-->
<!--  },-->
<!--  created() {-->
<!--    this.getUser().then(res => {-->
<!--      console.log(res)-->
<!--      this.form = res-->
<!--    })-->
<!--  },-->
<!--  methods: {-->
<!--    async getUser() {-->
<!--      return (await this.request.get("/user/username/" + this.user.username)).data-->
<!--    },-->
<!--    save() {-->
<!--      // this.request.post("/user", this.form).then(res => {-->
<!--      this.request.post("/staff", this.form).then(res => {-->
<!--        if (res.code === '200') {-->
<!--          this.$message.success("保存成功")-->

<!--          // 触发父级更新User的方法-->
<!--          this.$emit("refreshUser")-->

<!--          // 更新浏览器存储的用户信息-->
<!--          this.getUser().then(res => {-->
<!--            res.token = JSON.parse(localStorage.getItem("user")).token-->
<!--            localStorage.setItem("user", JSON.stringify(res))-->
<!--          })-->

<!--        } else {-->
<!--          this.$message.error("保存失败")-->
<!--        }-->
<!--      })-->
<!--    },-->
<!--    handleAvatarSuccess(res) {-->
<!--      this.form.avatarUrl = res-->
<!--    }-->
<!--  }-->
<!--}-->
<!--</script>-->

<!--<style>-->
<!--.avatar-uploader {-->
<!--  text-align: center;-->
<!--  padding-bottom: 10px;-->
<!--}-->
<!--.avatar-uploader .el-upload {-->
<!--  border: 1px dashed #d9d9d9;-->
<!--  border-radius: 6px;-->
<!--  cursor: pointer;-->
<!--  position: relative;-->
<!--  overflow: hidden;-->
<!--}-->
<!--.avatar-uploader .el-upload:hover {-->
<!--  border-color: #409EFF;-->
<!--}-->
<!--.avatar-uploader-icon {-->
<!--  font-size: 28px;-->
<!--  color: #8c939d;-->
<!--  width: 138px;-->
<!--  height: 138px;-->
<!--  line-height: 138px;-->
<!--  text-align: center;-->
<!--}-->
<!--.avatar {-->
<!--  width: 138px;-->
<!--  height: 138px;-->
<!--  display: block;-->
<!--}-->
<!--</style>-->

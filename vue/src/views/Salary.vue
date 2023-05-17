<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="searchname"></el-input>
      <el-select v-model="searchId" placeholder="请输入部门">
        <el-option v-for="item in depts" :key="item.id" :label="item.deptName" :value="item.id">
        </el-option>
      </el-select>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-popconfirm
          class="ml-5"
          confirm-button-text='确定'
          cancel-button-text='我再想想'
          icon="el-icon-info"
          icon-color="red"
          title="您确定批量删除这些数据吗？"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference">批量删除 <i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
      <el-upload :action="'http://' + serverIp + ':9090/salary/import'" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">
        <el-button type="primary" class="ml-5">导入 <i class="el-icon-bottom"></i></el-button>
      </el-upload>
      <el-button type="primary" @click="exp" class="ml-5">导出 <i class="el-icon-top"></i></el-button>
    </div>



<!--表格========================================-->
    <el-table style="font-size: 15px" :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange" >
      <el-table-column  type="selection" width="55"></el-table-column>
      <el-table-column STYLE="size: 20px" type="index" label="序号" width="55"></el-table-column>
      <el-table-column prop="avatarUrl" label="员工照片"  width="130px" >
        <template slot-scope="scope">
          <img :src="scope.row.avatarUrl" width="100px" height="100px">
        </template>
      </el-table-column>
      <el-table-column prop="username" label="员工工号" width="90px"></el-table-column>
      <el-table-column prop="nickname" label="姓名"></el-table-column>
      <el-table-column prop="gender" label="性别" width="65">
        <template slot-scope="scope">
          {{scope.row.gender===1?"女":"男"}}
        </template>
      </el-table-column>
      <el-table-column prop="deptName" label="部门名称" width="90"></el-table-column>
      <el-table-column prop="baseSalary" label="基础薪资" width="90"></el-table-column>
      <el-table-column prop="bonus" label="奖金" width="70"></el-table-column>
      <el-table-column prop="insure" label="五险一金" width="90"></el-table-column>
      <el-table-column prop="fine" label="罚款" width="65"></el-table-column>
      <el-table-column  prop="endSalary" label="应发薪水" width="120"></el-table-column>


      <el-table-column fixed="right" label="操作"  width="200" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>
<!--==========================================================弹窗-->
    <el-dialog style="font-size: 15px" title="用户信息" :visible.sync="dialogFormVisible" width="400px" >
      <el-form  ref="from" :model="form" :rules="rules" label-width="100px" size="small">
        <el-form-item  label="工号:">
          <span>{{form.username}}</span>
        </el-form-item>
          <el-form-item prop="nickname" label="姓名:">
          <span>{{form.nickname}}</span>
        </el-form-item>
        <el-form-item prop="deptName" label="部门:">
            <span>{{form.deptName}}</span>
        </el-form-item>
        <el-form-item prop="baseSalary" label="基础薪资:">
          <el-input v-model="form.baseSalary" autocomplete="on" style="width: 120px"></el-input>
        </el-form-item>
        <el-form-item prop="bonus" label="奖金:">
          <el-input v-model="form.bonus" autocomplete="off" style="width: 120px"></el-input>
        </el-form-item>
        <el-form-item   prop="insure" label="五险一金:" >
          <el-input v-model="form.insure" autocomplete="off" style="width: 120px"></el-input>
        </el-form-item>
        <el-form-item prop="bonus" label="罚款:" >
          <el-input v-model="form.fine" autocomplete="off" style="width: 120px"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel1">取 消</el-button>
        <el-button type="primary" @click="sava1">确 定</el-button>
      </div>
    </el-dialog>



<!--    新增！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！-->
    <!--==========================================================弹窗-->
    <el-dialog title="用户信息" :visible.sync="dialogNewFormVisible" width="400px"  >
      <el-form  ref="from" :model="form" :rules="rules" label-width="40px"  size="15px">
        <el-form-item prop="nickname" label="工号:">
          <el-input v-model="form.username" autocomplete="on"></el-input>
        </el-form-item>
        <el-form-item prop="nickname" label="姓名:">
          <el-input v-model="form.nickename" autocomplete="on"></el-input>
        </el-form-item>
        <el-form-item prop="baseSalary" label="基础薪资">
          <el-input v-model="form.baseSalary" autocomplete="on"></el-input>
        </el-form-item>
        <el-form-item prop="bonus" label="奖金">
          <el-input v-model="form.bonus" autocomplete="off" ></el-input>
        </el-form-item>
        <el-form-item  prop="insure" label="五险一金" >
          <el-input v-model="form.insure" autocomplete="off" size="mini"></el-input>
        </el-form-item>
        <el-form-item  prop="bonus" label="罚款" >
          <el-input v-model="form.fine" autocomplete="off" size="mini"></el-input>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel1">取 消</el-button>
        <el-button type="primary" @click="sava1">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {serverIp} from "../../public/config";

export default {
  name: "User",
  data() {
    return {
      serverIp: serverIp,
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 5,
      searchname: "",
      searchdeptName: "",
      searchId:"",
      form: {},
      depts: {},
      dialogNewFormVisible: false,
      dialogFormVisible: false,
      rules: {
        baseSalary: [
          {required: true, message: '请输入正确的数字', trigger: 'blur'},
          {pattern:/^([1-9][0-9]*|0)(\.[0-9]?[1-9])?$/, message: "请输入正确的数字", trigger: "blur"},
        ],
        bonus: [
          {required: true, message: '请输入正确的数字', trigger: 'blur'},
          {pattern: /^([1-9][0-9]*|0)(\.[0-9]?[1-9])?$/, message: "请输入正确的数字", trigger: "blur"}
        ],
        insure: [
          {required: true, message: "请输入正确的数字",trigger: "blur"},
          {pattern: /^([1-9][0-9]*|0)(\.[0-9]?[1-9])?$/, message: "请输入正确的数字", trigger: "blur"}
        ],
        fine: [
          {required: true, message: "请输入正确的数字",trigger: "blur"},
          {pattern: /^([1-9][0-9]*|0)(\.[0-9]?[1-9])?$/, message: "请输入正确的数字", trigger: "blur"}
        ]
      }
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("/salary/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          searchname: this.searchname,
          searchId: this.searchId
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
      this.request.get("/role").then(res => {
        this.roles = res.data
      })
      this.request.get("/dept").then(res => {
        this.depts = res.data
      })
    },
    cancel1(){
      this.dialogFormVisible = false,
          this.dialogNewFormVisible = false, this.$message.success('操作取消')
      this.load()
    },
    sava1() {

      this.$refs.from.validate((valid) => {
        if(!valid) {
          this.$message.warning('请调整数据后再提交')
          this.load()
        }else {
          this.request.post("/salary", this.form).then(res => {
            if (res.code === '200') {
              this.$message.success("保存成功")
              this.dialogFormVisible = false
              this.load()
            } else {
              this.$message.error("保存失败")
            }
          })
        }
      })
    },
    save() {
      this.request.post("/salary", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("保存失败")
        }
      })
    },
    handleAdd() {
      this.dialogNewFormVisible = true
      this.form = {}
    },
    handleEdit(row) {
      if (this.$refs.from != undefined) {
        this.$refs.from.resetFields();
      }
      this.form=row
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete("/salary/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/salary/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    handleExcelImportSuccess(response) {
        if (response.code === "200") {
          this.$message.success("导入成功")
          this.load()
        } else {
          this.$message.error("导入失败{"+response.msg +"}")
        }
    },
    reset() {
      this.searchname = ""
      this.searchId = ""
      this.load()
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
    exp() {
      window.open(`http://${serverIp}:9090/salary/export`)
    },
  }
}
</script>


<style>
.headerBg {
  background: #eee!important;
}
</style>

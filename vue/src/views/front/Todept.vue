<template>
  <div>

    <el-button type="primary" @click="handleAdd">创建申请 <i class="el-icon-circle-plus-outline"></i></el-button>

    <el-table style="font-size: 15px;" :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
      <el-table-column prop="nickname" label="申请人"></el-table-column>
      <el-table-column prop="dept_name" label="部门名称"></el-table-column>
      <el-table-column prop="newDept"  label="转入部门">
        <template slot-scope="scope">
          <el-tag v-for="dept in depts"
                  :key = "dept.id"
                  v-if="scope.row.newDept === dept.id"
          >{{dept.deptName}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="time" label="申请时间"></el-table-column>
      <el-table-column prop="state" label="申请状态"></el-table-column>
      <el-table-column label="操作"  width="180" align="center">
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
    <!-----------------------------------------------           编辑弹窗                       -->
    <el-dialog  title="信息" :visible.sync="dialogFormVisible" width="30%" :close-on-click-modal="false">
      <el-form label-width="100px" size="15px" style="width: 90%">
        <el-form-item label="姓名">
          <el-input v-model="form.nickname"></el-input>
        </el-form-item>
        <el-form-item label="所在部门">
          <el-input v-model="form.dept_name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="调入部门">
          <el-select clearable v-model="form.newDept" placeholder="请选择部门" style="width: 100%">
            <el-option v-for="item in depts" :key="item.id" :label="item.deptName" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="申请时间">
          <div class="block">
            <span class="demonstration"></span>
            <el-date-picker
                v-model="form.time"
                type="date"
                placeholder="选择日期">
            </el-date-picker>
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>


    <!--------------------------------------  新增  ----------------------------------->
    <el-dialog title="信息" :visible.sync="dialogNewFormVisible" width="30%" :close-on-click-modal="false">
      <el-form label-width="100px" size="small" style="width: 90%">
        <el-form-item label="姓名">
          <el-input v-model="form2.nickname"></el-input>
        </el-form-item>
        <el-form-item label="所在部门">
          <el-input v-model="form2.deptName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="调入部门">
          <el-select clearable v-model="form2.newDept" placeholder="请选择部门" style="width: 100%">
            <el-option v-for="item in depts" :key="item.id" :label="item.deptName" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="申请时间">
          <div class="block">
            <span class="demonstration"></span>
            <el-date-picker
                v-model="form2.time"
                type="date"
                placeholder="选择日期">
            </el-date-picker>
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogNewFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="add">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Todept",
  data() {
    return {
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      form: {},
      form2: {},
      dialogFormVisible: false,
      dialogNewFormVisible: false,
      multipleSelection: []
    }
  },
  created() {
    this.load()
  },
  methods: {
      load(){
        this.request.get("/todept/page2",
            {
              params:{
                pageNum:this.pageNum,
                pageSize:this.pageSize,
                id:this.user.id,
                name:this.name,
              }
            }
        ).then(res => {
          this.tableData = res.data.records
          this.total = res.data.total
        }),
            this.request.get("/dept").then(res => {
              this.depts = res.data
            })
    },
    add() {
      this.request.post("/todept", this.form2).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.dialogNewFormVisible = false
          this.load()
        } else {
          this.$message.error("保存失败")
        }
      })
    },
    save() {
      this.request.post("/todept", this.form).then(res => {
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
      this.form2.userId = this.user.id
      this.form2.nickname = this.user.nickname
      this.form2.deptId = this.user.deptId
      this.form2.deptName = this.user.deptName
      this.dialogNewFormVisible = true
    },
    handleEdit(row) {
      this.form = row
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete("/todept/" + id).then(res => {
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
    reset() {
      this.name = ""
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
    handleFileUploadSuccess(res) {
      this.form.file = res
    },
    download(url) {
      window.open(url)
    }
  }
}
</script>


<style>
.headerBg {
  background: #eee!important;
}
</style>

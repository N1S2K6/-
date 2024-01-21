<template>
  <div>
    <div style="margin-bottom: 5px">
      <!--查询-->
<!--      <el-input v-model="id" placeholder="请输入id" suffix-icon="el-icon-search" style="margin-left: 5px;width: 160px;"-->
<!--                @keyup.enter.native="loadPost">-->
<!--      </el-input>-->
      <el-select v-model="storage" filterable placeholder="请选择仓库名" style="margin-left: 5px;width: 160px">
        <el-option
          v-for="item in storageData"
          :key="item.id"
          :label="item.name"
          :value="item.id">
        </el-option>
      </el-select>
      <el-select v-model="goodsType" filterable placeholder="请选择类别" style="margin-left: 5px;width: 120px">
        <el-option
          v-for="item in goodsTypeData"
          :key="item.id"
          :label="item.name"
          :value="item.id">
        </el-option>
      </el-select>
      <el-input v-model="name" placeholder="请输入物品名" suffix-icon="el-icon-search" style="width: 160px;margin-left: 5px;"
                @keyup.enter.native="loadPost">
      </el-input>
<!--      <el-input v-model="count" placeholder="请输入数量" suffix-icon="el-icon-search" style="width: 160px;margin-left: 5px;"-->
<!--                @keyup.enter.native="loadPost">-->
<!--      </el-input>-->
<!--      <el-input v-model="remark" placeholder="请输入备注" suffix-icon="el-icon-search" style="width: 160px;margin-left: 5px;"-->
<!--                @keyup.enter.native="loadPost">-->
<!--      </el-input>-->

      <el-button type="primary" style="margin-left: 5px" @click="loadPost">查询</el-button>
      <el-button type="success" @click="resetParam">重置</el-button>

    </div>
    <!--表-->
    <el-table :data="tableData"
              :header-cell-style="{background:'#f3f6fd',color:'#555555'}"
              border
    >
      <el-table-column prop="id" label="ID" width="60">
      </el-table-column>

      <el-table-column prop="goodsname" label="物品名" width="150">
      </el-table-column>

      <el-table-column prop="storagename" label="仓库" width="150">
      </el-table-column>

      <el-table-column prop="goodstypename" label="分类" width="150">
      </el-table-column>

      <el-table-column prop="adminname" label="操作人" width="150">
      </el-table-column>

      <el-table-column prop="username" label="申请人" width="150">
      </el-table-column>

      <el-table-column prop="count" label="数量" width="150">
      </el-table-column>

      <el-table-column prop="create_time" label="操作时间" width="150">
      </el-table-column>

      <el-table-column prop="remark" label="备注">
      </el-table-column>

      <!--    实现分页-->
    </el-table>
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
</template>

<script>
export default {
  name: "RecordManage",
  data() {
    return {
      user: JSON.parse(sessionStorage.getItem('CurUser')),
      storageData: [],
      goodsTypeData: [],
      tableData: [],
      pageSize: 10,
      pageNum: 1,
      total: 0,
      id: '',
      name: '',
      storage: '',
      goodsType: '',
      count: '',
      remark: '',
      centerDialogVisible: false,
      form: {
        id: '',
        name: '',
        storage: '',
        goodsType: '',
        count: '',
        remark: '',
      },
    };
  },
  methods:{
    formatStorage(row) {
      let temp = this.storageData.find(item=>{
        return item.id == row.storage
      })
      return temp && temp.name
    },
    formatGoodsType(row) {
      let temp = this.goodsTypeData.find(item=>{
        return item.id == row.goodsType
      })
      return temp && temp.name
    },
    resetForm() {
      this.form.id = ''
      this.form.name = ''
      this.form.storage = ''
      this.form.goodsType = ''
      this.form.count = ''
      this.form.remark = ''
    },
    handleClose(done) {
      this.$confirm('确认关闭？')
        .then(_ => {
          done();
        })
        .catch(_ => {});
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.pageNum=1
      this.pageSize=val
      this.loadPost()
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.pageNum=val
      this.loadPost()
    },
    resetParam(){
      this.id=''
      this.name=''
      this.storage=''
      this.goodsType=''
      this.count=''
      this.remark=''
    },
    loadPost(){
      this.$axios.post(this.$httpUrl+'/record/listPage',{
        pageSize:this.pageSize,
        pageNum:this.pageNum,
        param:{
          id:this.id,
          name:this.name,
          goodsType: this.goodsType+'',
          storage: this.storage+'',
          roleId:this.user.roleId+'',
          userId:this.user.id+'',
          count: this.count,
          remark: this.remark,
        }
      }).then(res=>res.data).then(res=>{
        console.log(res)
        if (res.code==200) {
          this.tableData=res.data
          this.total=res.total
        }else {
          alert('数据获取失败')
        }
      })
    },
    loadStorage() {
      this.$axios.get(this.$httpUrl+'/storage/list',).then(res=>res.data).then(res=>{
        console.log(res)
        if (res.code==200) {
          this.storageData=res.data
        } else {
          alert('数据获取失败')
        }

      })
    },
    loadGoodsType() {
      this.$axios.get(this.$httpUrl+'/goodstype/list',).then(res=>res.data).then(res=>{
        console.log(res)
        if (res.code==200) {
          this.goodsTypeData=res.data
        } else {
          alert('数据获取失败')
        }

      })
    },
  },

  beforeMount() {
    this.loadStorage()
    this.loadGoodsType()
    this.loadPost()
  }
}
</script>



<style scoped>

</style>


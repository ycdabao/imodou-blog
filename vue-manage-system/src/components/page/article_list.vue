<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-cascades"></i> 文章信息
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-button
                    type="primary"
                    icon="el-icon-delete"
                    class="handle-del mr10"
                    @click="delAllSelection"
                >批量删除</el-button>
                <el-input v-model="query.data.title" placeholder="标题" class="handle-input mr10"></el-input>
                <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
                  <el-button type="primary" icon="el-icon-search" @click="handleAdd">添加</el-button>
            </div>
            <el-table
                :data="tableData"
                border
                class="table"
                ref="multipleTable"
                header-cell-class-name="table-header"
                @selection-change="handleSelectionChange"
            >
                <el-table-column type="selection" width="55" align="center"></el-table-column>
                <el-table-column prop="id" label="ID" width="55" align="center"></el-table-column>
                <el-table-column prop="title" label="标题" width="355" align="center"></el-table-column>
                <el-table-column prop="userId" label="作者" align="center"></el-table-column>
                <el-table-column prop="pubdate" label="发表日期" align="center"></el-table-column>
                <el-table-column prop="modifydate" label="修改日期" align="center"></el-table-column>
                <el-table-column prop="views" label="浏览次数" width="80" align="center"></el-table-column>
                <el-table-column prop="commentCount" label="评论次数" width="80" align="center"></el-table-column>
                <el-table-column prop="likeCount" label="收藏次数" width="80" align="center"></el-table-column>
                 <el-table-column prop="status" label="状态" align="center"></el-table-column>

                <el-table-column label="操作" width="180" align="center">
                    <template slot-scope="scope">
                        <el-button
                            type="text"
                            icon="el-icon-edit"
                            @click="handleEdit(scope.$index, scope.row)"
                        >编辑</el-button>
                        <el-button
                            type="text"
                            icon="el-icon-delete"
                            class="red"
                            @click="handleDelete(scope.$index, scope.row)"
                        >删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination
                    background
                    layout="total, prev, pager, next"
                    :current-page="query.pageIndex"
                    :page-size="query.pageSize"
                    :total="pageTotal"
                    @current-change="handlePageChange"
                ></el-pagination>
            </div>
        </div>


     
    </div>
</template>

<script>
import { fetchData } from '../../api/index';
import Axios from 'axios';
export default {
    name: 'basetable',
    data() {
        return {
            query: {
                data:{},
                pageIndex: 1,
                pageSize: 10
            },
            tableData: [],
            multipleSelection: [],
            delList: [],

            pageTotal: 0,
            form: {},
            idx: -1,
            id: -1,
            options:[]
        };
    },
    created() {
        this.getData();
    },
    methods: {
   
        getData() {

          Axios.post("/api/article/findpage",this.query).then((response)=>{
             
             if(response.data.flag){
                this.$message.success(response.data.message);
                this.tableData=response.data.data.rows;
                this.pageTotal=response.data.data.total;   
             }else{
                 this.$message.error(response.data.message);
             }
         
          })
        },
        // 触发搜索按钮
        handleSearch() {
            this.query.pageIndex=1;
             Axios.post("/api/article/findpage",this.query).then((response)=>{
             
             if(response.data.flag){
                this.$message.success(response.data.message);
                this.tableData=response.data.data.rows;
                this.pageTotal=response.data.data.total;   
             }else{
                 this.$message.error(response.data.message);
             }
         
          })
        },
        // 删除操作
        handleDelete(index, row) {
            // 二次确认删除
            this.$confirm('确定要删除吗？', '提示', {
                type: 'warning'
            })
                .then(() => {
                    this.$message.success('删除成功');
                    this.tableData.splice(index, 1);
                })
                .catch(() => {});
        },
        // 多选操作
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        delAllSelection() {
            const length = this.multipleSelection.length;
            let str = '';
            this.delList = this.delList.concat(this.multipleSelection);
            for (let i = 0; i < length; i++) {
                str += this.multipleSelection[i].name + ' ';
            }
            this.$message.error(`删除了${str}`);
            this.multipleSelection = [];
        },
        // 编辑操作
        handleEdit(index, row) {
                   
                    localStorage.setItem('article_id', row.id);
                    this.$router.push('/article_edit');
        },
    
        // 分页导航
        handlePageChange(val) {
            this.$set(this.query, 'pageIndex', val);
            this.getData();
        },
        // 添加操作
        handleAdd() {
            this.form={};
            this.addVisible = true;
            //查询父分类树
            Axios.get("/api/category/tree").then((response)=>{
                this.options=response.data;
             
            })
            
        },
        //选择父类
        handleChange(value){
 
            this.form.parentid=value[value.length-1];
          
        },
        // 保存添加
        saveAdd() {
           
            Axios.post("/api/category",this.form).then((response)=>{
                if(response.data.flag){
                    this.$message.success(response.data.message);
                }else{
                    this.$message.error(response.data.message);
                }
            })
             this.addVisible = false;
            this.getData();
        },
    }
};
</script>

<style scoped>
.handle-box {
    margin-bottom: 20px;
}

.handle-select {
    width: 120px;
}

.handle-input {
    width: 300px;
    display: inline-block;
}
.table {
    width: 100%;
    font-size: 14px;
}
.red {
    color: #ff0000;
}
.mr10 {
    margin-right: 10px;
}
.table-td-thumb {
    display: block;
    margin: auto;
    width: 40px;
    height: 40px;
}
</style>

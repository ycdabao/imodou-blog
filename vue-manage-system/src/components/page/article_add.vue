<template>

    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-lx-calendar"></i> 文章</el-breadcrumb-item>
                <el-breadcrumb-item>发布</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
           <div class="form-box" style="width:98%">
                <el-form ref="form" :model="form" label-width="80px">
                    <el-form-item label="标题">
                        <el-input v-model="form.title"></el-input>
                    </el-form-item>
                    <el-form-item label="摘要">
                        <el-input v-model="form.summary" style="width:100%"></el-input>
                    </el-form-item>
                              <el-form-item label="父类">
                    <el-cascader ref="categoryCascader" :v-model="form.categories"
                       
                        :options="options"
                     
                        @change="handleChange"   ></el-cascader>
                </el-form-item>
                    <el-form-item label="封面">
                        <div class="crop-demo">
                            <img :src="cropImg" class="pre-img" style="width:150px;height:100px">
                            <div class="crop-demo-btn">选择图片
                                <input class="crop-input" type="file" name="image" accept="image/*" @change="setImage"/>
                            </div>
                        </div>
                
                        <el-dialog title="裁剪图片" :visible.sync="dialogVisible" width="30%">
                            <vue-cropper ref='cropper' :src="imgSrc" :ready="cropImage" :zoom="cropImage" :cropmove="cropImage" style="width:100%;height:300px;"></vue-cropper>
                            <span slot="footer" class="dialog-footer">
                                <el-button @click="cancelCrop">取 消</el-button>
                                <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
                            </span>
                        </el-dialog>
                    </el-form-item>
                </el-form>
           </div>
            <mavon-editor v-model="content" ref="md" @imgAdd="$imgAdd" @change="change" style="min-height: 600px"/>
            <el-button class="editor-btn" type="primary" @click="submit">提交</el-button>
        </div>
    </div>
</template>

<script>
    import { mavonEditor } from 'mavon-editor'
    import 'mavon-editor/dist/css/index.css'
    import VueCropper  from 'vue-cropperjs';
import Axios from 'axios';
    export default {
        name: 'markdown',
        data: function(){
            return {
                content:'',
                html:'',
                configs: {
                },
                form: {
               
                },
                imgSrc: '',
                cropImg: '',
                dialogVisible: false,
                defaultSrc: require('../../assets/img/img.jpg'),
                options:[]
            }
        },
        components: {
            mavonEditor,
            VueCropper
        },
        methods: {
            // 将图片上传到服务器，返回地址替换到md中
            $imgAdd(pos, $file){
                var formdata = new FormData();
                formdata.append('file', $file);
                // 这里没有服务器供大家尝试，可将下面上传接口替换为你自己的服务器接口
                Axios({
                    url: '/api/article/upload',
                    method: 'post',
                    data: formdata,
                    headers: { 'Content-Type': 'multipart/form-data' },
                }).then((res) => {
                    this.$refs.md.$img2Url(pos, res.data.data);
                })
            },
            change(value, render){
                // render 为 markdown 解析后的结果
                this.html = render;
            },
            submit(){
                // console.log(this.form);
                // console.log(this.content);
                // console.log(this.html);

                this.form.content=this.content;
                Axios.post("/api/article",this.form).then((res)=>{
                    if(res.data.flag){
                          this.$message.success('提交成功！');
                    }else{
                        this.$message.error(res.data.message);
                    }
                    
                })
                

              
            },
            setImage(e){
               
                const file = e.target.files[0];
                
                if (!file.type.includes('image/')) {
                    return;
                }
                const reader = new FileReader();
                reader.onload = (event) => {
              
                    this.dialogVisible = true;
                    this.imgSrc = event.target.result;
                    this.$refs.cropper && this.$refs.cropper.replace(event.target.result);
                };
                reader.readAsDataURL(file);
                
                let formData = new FormData()
                formData.append('file',e.target.files[0])
                Axios({
                    url:"/api/article/upload",
                    method: 'post',
                    data: formData,
                    processData: false,// 告诉axios不要去处理发送的数据(重要参数)
                    contentType: false,   // 告诉axios不要去设置Content-Type请求头
                }).then((res)=>{
                    if(res.data.flag){
                        this.form.coverPhoto=res.data.data;
                    }else{
                        this.$message.error(res.data.message);
                    }
                    
                })
            

            },
            cropImage () {
                this.cropImg = this.$refs.cropper.getCroppedCanvas().toDataURL();
            },
            cancelCrop(){
                this.dialogVisible = false;
                this.cropImg = this.defaultSrc;
            },
            imageuploaded(res) {
                console.log(res)
            },
            handleError(){
                this.$notify.error({
                    title: '上传失败',
                    message: '图片上传接口上传失败，可更改为自己的服务器接口'
                });
            },
                    //选择父类
            handleChange(value){
    
                this.form.categories=value;
              
            }

        },
        created(){
            this.cropImg = this.defaultSrc;
            //查询分类
            Axios.get("/api/category/tree").then((response)=>{
            this.options=response.data;
            
        })
        }
    }
</script>
<style scoped>
    .editor-btn{
        margin-top: 20px;
    }
</style>
<style scoped>
    .content-title{
        font-weight: 400;
        line-height: 50px;
        margin: 10px 0;
        font-size: 22px;
        color: #1f2f3d;
    }
    .pre-img{   
        width: 100px;
        height: 100px;
        background: #f8f8f8;
        border: 1px solid #eee;
        border-radius: 5px;
    }
    .crop-demo{
        display: flex;
        align-items: flex-end;
    }
    .crop-demo-btn{
        position: relative;
        width: 100px;
        height: 40px;
        line-height: 40px;
        padding: 0 20px;
        margin-left: 30px;
        background-color: #409eff;
        color: #fff;
        font-size: 14px;
        border-radius: 4px;
        box-sizing: border-box;
    }
    .crop-input{
        position: absolute;
        width: 100px;
        height: 40px;
        left: 0;
        top: 0;
        opacity: 0;
        cursor: pointer;
    }
</style>
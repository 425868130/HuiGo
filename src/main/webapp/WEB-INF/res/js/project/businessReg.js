/**
 * 商家注册页面js文件
 */
var app = new Vue({
    el: '#app',
    data: {
        url: routerUrl,
        currentStep: 0,
        tipsData: [],
        saleTypeList: [],
        fileNum: 0,
        loading: false,
        formStp: {
            account: '',
            password: '',
            passwordCheck: '',
            tel: '',
            mail: '',
            shopName: '',
            realName: '',
            idCard: '',
            saleType: '',
            licensePic: '',
            licenseCode: ''
        },
        ruleValidate: {
            account: [
                {required: true, message: '账号不能为空', trigger: 'blur'},
                {type: 'string', min: 6, message: '账号长度不能小于6位', trigger: 'blur'}
            ],
            password: [
                {required: true, message: '密码不能为空', trigger: 'blur'},
                {type: 'string', min: 6, message: '密码长度不能小于6位', trigger: 'blur'}
            ],
            passwordCheck: [
                {required: true, message: '请输入验证密码', trigger: 'blur'},
                {type: 'string', min: 6, message: '密码长度不能小于6位', trigger: 'blur'}
            ],
            tel: [
                {required: true, message: '手机号码不能为空', trigger: 'blur'},
                {min: 11, max: 11, message: '手机号码长度错误', trigger: 'blur'}
            ],
            mail: [
                {required: true, message: '邮箱不能为空', trigger: 'blur'},
                {type: 'email', message: '邮箱格式不正确', trigger: 'blur'}
            ],
            shopName: [
                {required: true, message: '店铺不能为空', trigger: 'blur'},
                {min: 2, max: 11, message: '请输入正确长度的店铺名', trigger: 'blur'}
            ],
            realName: [{required: true, message: '姓名不能为空', trigger: 'blur'}],
            idCard: [{required: true, message: '身份证不能为空', trigger: 'blur'}],
            saleType: [{required: true, message: '请选择销售类型'}],
            licensePic: [{required: true, message: '请上传营业执照'}],
            licenseCode: [{required: true, message: '请填写营业执照编号', trigger: 'blur'}]
        }
    },
    methods: {
        nextStep: function (name) {
            /*如果校验失败则不进入下一步*/
            if (!formCheck(name)) {
                return;
            }
            /*如果在第一步,则进入下一步*/
            if (this.currentStep === 0) {
                this.currentStep += 1;
            }
            /*此时提交注册表单*/
            else if (this.currentStep === 1) {
                this.loading = true;
                Ajx.post(routerUrl.post.businessReg, app.formStp).then(function (response) {
                    if (response.data == true) {
                        app.currentStep++;
                        app.loading = false;
                    }
                }).catch(function (error) {
                    app.$Modal.error({
                        title: '注册失败',
                        content: error.response.data.message
                    });
                    app.loading = false;
                })
            }
            else if (this.currentStep === 2) {
                this.currentStep = 0;
            }
        },
        preStep: function () {
            if (this.currentStep === 1) {
                this.currentStep -= 1;
            }
        },
        /*开发时用于跳过校验预览后续步骤页面*/
        fakeStep: function () {
            this.currentStep += 1;
        },
        handleSearch: function (value) {
            /*邮箱输入的自动补全*/
            this.tipsData = !value || value.indexOf('@') >= 0 ? [] : [
                value + '@qq.com',
                value + '@sina.com',
                value + '@163.com'
            ];
        },
        uploadFailed: function (file) {
            if (this.fileNum == 1) {
                this.fileNum = 0;
            }
            this.$Notice.error({
                title: '上传失败,请重试!'
            });
        },
        handleBeforeUpload: function () {
            /*只能上传一张照片*/
            if (this.fileNum == 1) {
                this.$Notice.error({
                    title: '请勿重复上传!'
                });
                return false;
            }
            this.fileNum = 1;
            return true;
        },
        /*文件上传完成获取图片url*/
        handleSuccess: function (response) {
            app.formStp.licensePic = response;
            this.$Notice.success({
                title: '营业执照已上传!'
            });
        }
    },
    watch: {
        currentStep: function () {
            /*第二步才加载销售类型表*/
            if (this.currentStep != 1) {
                return;
            }
            Ajx.get(routerUrl.get.saleTypeList).then(function (response) {
                response.data.forEach(function (type) {
                    app.saleTypeList.push(type);
                })
            }).catch(function () {
                this.saleTypeList = [{id: '', name: ''}];
            });
        }
    }
});


/*表单校验*/
function formCheck(name) {
    var result;
    app.$refs[name].validate(function (valid) {
        if (valid) {
            /*在第一步时需要额外判断两次密码是否相同*/
            if (app.currentStep == 0 && app.formStp.password != app.formStp.passwordCheck) {
                app.$Modal.error({
                    title: '密码错误',
                    content: '请输入两次相同的密码!'
                });
                result = false;
            } else {
                result = true;
            }
        } else {
            app.$Modal.error({
                title: '注册失败',
                content: '请仔细检查信息是否输入有误'
            });
            result = false;
        }
    });
    return result;
}
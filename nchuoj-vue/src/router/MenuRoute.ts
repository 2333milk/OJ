import ACCESS_ENUM from "@/access/accessEnum";

const MenuRoute = [
    {
        path:'/list/manageQuestion',
        name:"题目管理",
        meta:{
            access:ACCESS_ENUM.ADMIN,
            needLogin:true,
            ordinary:false,
        },
        component:()=>import("@/views/question/ManageQuestionView.vue")
    },{
        path:'/list/question',
        name:"题目列表",
        meta:{
            access:ACCESS_ENUM.USER,
            needLogin:true,
            ordinary:true,
        },
        component:()=>import("@/views/question/ListQuestionView.vue")
    },{
        path:'/list/submitQuestion',
        name:"判题列表",
        meta:{
            access:ACCESS_ENUM.USER,
            needLogin:true,
            ordinary:true,
        },
        component:()=>import("@/views/question/ListSubmitQuestionView.vue")
    },
]

export default MenuRoute;
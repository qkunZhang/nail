import axios from "axios";
import localforage from 'localforage';
const http = axios.create(
	{ 
		baseURL: "http://localhost:8080", //这里配置的是后端服务提供的接口
		timeout: 30000 ,
		method:"post",
		headers: {
			'Content-Type': 'application/json'
		  },
	}
);

// 添加请求拦截器
http.interceptors.request.use(
	(config)=> {
		const authDB = localforage.createInstance({
			name: "authDB"
		  });
    	config.headers.Authorization = authDB.getItem("accessJWT") 
		console.log(config.headers.Authorization);
    	return config;
	}, 
	(error)=> {
    	return Promise.reject(error);
	}
)

// 响应拦截器
http.interceptors.response.use(
	(res)=>{
		let code = res.data.code  // 获取后端返回的状态码
		if(code===200){           // 成功
			return res.data.data  // 返回里面的数据，在使用这个axios时，获取到的东西就是这里返回的东西
		}else{
			return res.data   
		}
	},
	(error)=>{
		return Promise.reject(error);
	}
)




export default http;

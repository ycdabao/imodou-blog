import request from '../utils/request';

export const fetchData = query => {
    return request({
       // url: './table.json',
       url: 'http://127.0.0.1/category/findFirstLevel',
        method: 'get',
        //params: query
    });
};

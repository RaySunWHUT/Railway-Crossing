const Mock = require('mockjs')

let dataList = Mock.mock({
  "comments|3-5": [{
      title: "@title(3)",
      author: "@name(3)",
      article: "@cparagraph)",
      star: 11
  }]
});

Mock.mock('/parameter/demoList', 'get', (res, req) => {
  return {
    status: 200,
    data: dataList,
    message: "成功"
  }
});


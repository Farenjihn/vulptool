import React from "react";

import {Layout} from 'antd';

const {Content} = Layout;

class Welcome extends React.Component {

  render() {
    return (
      <Content style={{ margin: "16px 16px" }}>
        <h1 style={{ margin: '30px 30px', color:'black', textAlign:'center'}}>
          Welcome to Vulptool !
        </h1>
      </Content>
    );
  }
}

export default Welcome;



import React from "react";

import {Layout} from 'antd';

const {Content} = Layout;

var bg=require('../img/a009841fe4b23bd268d760cd3d969dd1.jpg')

class Welcome extends React.Component {

  render() {
    return (
      <Content style={{backgroundColor: 'black', backgroundImage: "url('"+bg+"')", backgroundSize: 'cover', marginTop:'-60px' }}>
        <h1 style={{ margin: '30px 30px', color:'white', textAlign:'center'}}>
          Welcome to Vulptool !
        </h1>
      </Content>
    );
  }
}

export default Welcome;



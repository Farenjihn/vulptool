import React from "react";
import '../css/App.css';
import Calendar from './Calendar';
import Welcome from './Welcome';
import Roster from './Roster';
import Login from './Login';
import Register from "./Register";


import FormEventEdit from './FormEventEdit.js'
import RosterCreate from './RosterCreate.js';

import {BrowserRouter as Router, Link, Route} from 'react-router-dom';
import { Icon, Layout, Menu} from 'antd';

const {Header, Footer, Sider} = Layout;


//Conservation of selected menu item when reload.
let currentUrl = window.location.href;
let currentItem = currentUrl.replace("http://localhost:3000", "").split('/')[1];

class SiderDemo extends React.Component {
  state = {
    collapsed: false,
    current: currentItem,
  };
  onCollapse = (collapsed) => {
    console.log(collapsed);
    this.setState({collapsed});
  }
  handleMenuClick = (e) => {
    console.log('click ', e);
    this.setState({
      current: e.key,
    });
  }

  render() {
    return (
      <Router>
        <div>
          <Layout style={{minHeight: '100vh'}}>
            <Header className="header">
              <div className="add-button">
                <Menu theme="dark" mode="horizontal">
                  <Menu.Item key="login">
                    <Link to={"/login"}/>
                    <span>Login</span>
                  </Menu.Item>
                  <Menu.Item key="register">
                    <Link to={"/register"}/>
                    <span>Register</span>
                  </Menu.Item>
                  {/*<SubMenu title={<span><Icon type="setting" />Account</span>}>
                                <MenuItemGroup title="Item 1">
                                    <Menu.Item key="setting:1">Settings</Menu.Item>
                                    <Menu.Item key="setting:2">Characters</Menu.Item>
                                </MenuItemGroup>
                                <MenuItemGroup title="Item 2">
                                    <Menu.Item key="setting:3">Logout</Menu.Item>
                                </MenuItemGroup>
                            </SubMenu>*/}
                  <Menu.Item key="feedback">
                    <a href="https://google.com" target="_blank" rel="noopener noreferrer">{<span><Icon type="mail"/>Feedback</span>}</a>
                  </Menu.Item>
                </Menu>
              </div>
            </Header>
            <Layout>
              <Sider
                collapsible
                collapsed={this.state.collapsed}
                onCollapse={this.onCollapse}
              >
                <Menu theme="dark" mode="inline"
                      onClick={this.handleMenuClick}
                      selectedKeys={[this.state.current]}
                >
                  <Menu.Item key="welcome">
                    <Link to={"/welcome"}/>
                    <Icon type="home"/>
                    <span>Home</span>
                  </Menu.Item>
                  <Menu.Item key="calendar">
                    <Link to={"/calendar"}/>
                    <Icon type="calendar"/>
                    <span>Calendar</span>
                  </Menu.Item>
                  <Menu.Item key="roster">
                    <Link to={"/roster"}/>
                    <Icon type="team"/>
                    <span>Roster</span>
                  </Menu.Item>
                </Menu>
              </Sider>
              <Route exact path="/" component={Welcome}/>
              <Route exact path="/welcome" component={Welcome}/>
              <Route exact path="/calendar" component={Calendar}/>
              <Route exact path="/calendar/edit/:id" component={FormEventEdit}/>
              <Route exact path="/roster" component={Roster}/>
              <Route exact path="/roster/create" component={RosterCreate}/>
              <Route exact path="/login" component={Login}/>
              <Route exact path="/register" component={Register}/>
            </Layout>
            <Footer style={{height: '48px', textAlign: 'center'}}>
              Ant Design ©2016 Created by Ant UED
            </Footer>
          </Layout>
        </div>
      </Router>
    );
  }
}


export default SiderDemo;

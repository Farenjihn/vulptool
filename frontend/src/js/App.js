import React from "react";
import '../css/App.css';
import Calendar from './Calendar';
import Welcome from './Welcome';
import Roster from './Roster';


import {BrowserRouter as Router, Link, Route} from 'react-router-dom';
import {Card, Icon, Layout, Menu} from 'antd';

const {Meta} = Card;
const {Header, Content, Footer, Sider} = Layout;
const SubMenu = Menu.SubMenu;
const MenuItemGroup = Menu.ItemGroup;
const IconText = ({type, text}) => (
  <span>
    <Icon type={type} style={{marginRight: 8}}/>
    {text}
  </span>
);


class SiderDemo extends React.Component {
  state = {
    collapsed: false,
    current: 'welcome',
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
          <Header className="header">
            <div className="add-button">
              <Menu theme="dark" mode="horizontal">
                <Menu.Item key="login">
                  Login
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
          <Layout style={{minHeight: '100vh'}}>
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
            <Route path="/welcome" component={Welcome}/>
            <Route path="/calendar" component={Calendar}/>
            <Route path="/roster" component={Roster}/>
          </Layout>
          <Footer style={{height: '48px', textAlign: 'center'}}>
            Ant Design ©2016 Created by Ant UED
          </Footer>
        </div>
      </Router>
    );
  }
}


export default SiderDemo;

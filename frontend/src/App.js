import React from "react";
import './App.css';
import Calendar from './Calendar';
import Welcome from './Welcome';



import {
    BrowserRouter as Router,
    Route,
    Link
} from 'react-router-dom';
import { Layout, Menu, Breadcrumb, Icon } from 'antd';
import { List, Row, Col } from 'antd';
import { Card, Avatar } from 'antd';
const { Meta } = Card;
const { Header, Content, Footer, Sider } = Layout;
const SubMenu = Menu.SubMenu;
const IconText = ({ type, text }) => (
    <span>
    <Icon type={type} style={{ marginRight: 8 }} />
        {text}
  </span>
);



class SiderDemo extends React.Component {
    state = {
        collapsed: false,
    };
    onCollapse = (collapsed) => {
        console.log(collapsed);
        this.setState({ collapsed });
    }
    render() {
        return (
            <Router>
                <div>
                    <Header className="header">
                        <div className="logo" />
                        <Menu
                            theme="dark"
                            mode="horizontal"
                            defaultSelectedKeys={['3']}
                            style={{lineHeight: '64px' }}
                        >
                            <Menu.Item key="1">nav 1</Menu.Item>
                            <Menu.Item key="2">nav 2</Menu.Item>
                            <Menu.Item key="3">nav 3</Menu.Item>
                        </Menu>
                    </Header>
                    <Layout style={{ minHeight: '100vh' }}>
                        <Sider
                            collapsible
                            collapsed={this.state.collapsed}
                            onCollapse={this.onCollapse}
                        >
                            <Menu theme="dark" defaultSelectedKeys={['1']} mode="inline">
                                <Menu.Item key="1">
                                    <Link to={"/welcome"} />
                                    <Icon type="home" />
                                    <span>Home</span>
                                </Menu.Item>
                                <Menu.Item key="2">
                                    <Link to={"/calendar"} />
                                    <Icon type="calendar" />
                                    <span>Calendar</span>
                                </Menu.Item>
                                <Menu.Item key="3">
                                    <Link to={"/roster"} />
                                    <Icon type="team" />
                                    <span>Roster</span>
                                </Menu.Item>
                            </Menu>
                        </Sider>
                        <Route path="/welcome" component={Welcome}/>
                        <Route path="/calendar" component={Calendar}/>
                    </Layout>
                    <Footer style={{height: '48px', textAlign: 'center' }}>
                        Ant Design ©2016 Created by Ant UED
                    </Footer>
                </div>
            </Router>
        );
    }
}


export default SiderDemo;

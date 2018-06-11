import React from "react";
import FormRoster from './FormRoster'

import {Avatar, Button, Card, DatePicker, Icon, Layout, List, Menu} from "antd";
import { Table, Divider } from 'antd';

import { Tabs, Radio } from 'antd';
import { Form, Input } from 'antd';


const FormItem = Form.Item;

const TabPane = Tabs.TabPane;

const {Header, Content, Footer, Sider} = Layout;


const columns = [{
  title: 'Name',
  dataIndex: 'name',
  key: 'name',
  render: text => <a href="javascript:;">{text}</a>,
}, {
  title: 'ilvl',
  dataIndex: 'ilvl',
  key: 'ilvl',
}, {
  title: 'Player',
  dataIndex: 'player',
  key: 'player'
}, {
    title: 'Action',
    key: 'action',
    render: (text, record) => (
      <span>
      <a href="javascript:;">Action 一 {record.name}</a>
      <Divider type="vertical" />
      <a href="javascript:;">Delete</a>
      <Divider type="vertical" />
      <a href="javascript:;" className="ant-dropdown-link">
        More actions <Icon type="down" />
      </a>
    </span>
    ),
  }];

const data = [{
  key: '1',
  name: 'Bykowpal',
  ilvl: 980,
  player: 'Bykow',
}, {
  key: '2',
  name: 'Koma',
  ilvl: 981,
  player: 'Fatcream',
}, {
  key: '3',
  name: 'Crazydiamond',
  ilvl: 982,
  player: 'Crazy',
}];

let uuid = 0;

class Roster extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      mode: 'left', // position of tabs
    };
  }



  render() {
    const { mode } = this.state;
    return (
      <Content style={{margin: "16px 16px"}}>
        <div style={{padding: 24, background: "#fff", minHeight: 360}}>

          <div>
            <Button type="primary" onClick={this.showModal}>
              New Roster
            </Button>
            <FormRoster
              wrappedComponentRef={this.saveFormRef}
              visible={this.state.formVisible}
              onCancel={this.handleCancel}
              onCreate={this.handleCreate}
            />
          </div>

          <Tabs
            defaultActiveKey="1"
            tabPosition={mode}
            style={{ minHeight: 220 }}
          >
            <TabPane tab="Roster 1" key="1">
              <Table columns={columns} dataSource={data} />
            </TabPane>
            <TabPane tab="Roster 2" key="2">Content of tab 2</TabPane>
            <TabPane tab="Roster 3" key="3">Content of tab 3</TabPane>
            <TabPane tab="Roster 4" key="4">Content of tab 4</TabPane>
            <TabPane tab="Roster 5" key="5">Content of tab 5</TabPane>
            <TabPane tab="Roster 6" key="6">Content of tab 6</TabPane>
            <TabPane tab="Roster 7" key="7">Content of tab 7</TabPane>
            <TabPane tab="Roster 8" key="8">Content of tab 8</TabPane>
            <TabPane tab="Roster 9" key="9">Content of tab 9</TabPane>
            <TabPane tab="Roster 10" key="10">Content of tab 10</TabPane>
            <TabPane tab="Roster 11" key="11">Content of tab 11</TabPane>
          </Tabs>

        </div>
      </Content>
    );
  }
}


export default Roster;
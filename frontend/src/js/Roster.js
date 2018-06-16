import React from "react";
import FormRoster from './RosterCreate'
import '../css/App.css';

import {BrowserRouter as Router, Link, Route} from 'react-router-dom';

import {Button, Divider, Form, Icon, Layout, Table, Tabs} from "antd";
import {Input, AutoComplete } from 'antd';

import * as conf from "./config.js";


const FormItem = Form.Item;

const TabPane = Tabs.TabPane;

const {Header, Content, Footer, Sider} = Layout;

const Option = AutoComplete.Option;
const OptGroup = AutoComplete.OptGroup;


const columns = [{
  title: 'Name',
  dataIndex: 'name',
  key: 'name',
  render: text => <a href="javascript:;">{text}</a>,
}, {
  title: 'ilvl',
  dataIndex: 'ilvl',
  key: 'ilvl',
},
//   {
//   title: 'Player',
//   dataIndex: 'player',
//   key: 'player'
// },
  {
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


class Roster extends React.Component {
  constructor() {
    super();
    this.state = {
      rosters: [],
      mode: 'left', // position of tabs
      formVisible: false
    };

    fetch(conf.baseURL + '/roster', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      },
    })
      .then(results => results.json())
      .then(data => this.setState({rosters:data}))
      .catch(function (error) {
        console.log(
          "There was an error GET roster: /// " + error + " \\\\\\"
        );
      });
  }

  createTabs = () => {
    let tabs = [];

    for (let i = 0; i < this.state.rosters.length; i++) {
      if (this.state.rosters[i].figures.length > 0) {
        tabs.push(
          <TabPane tab={this.state.rosters[i].name} key={this.state.rosters[i].id}>
            <Table columns={columns} dataSource={this.state.rosters[i].figures} pagination={false}/>
          </TabPane>
        )
      }
    }
    return tabs
  }

  componentDidMount() {}

  render() {
    const { mode } = this.state;
    return (
      <Content style={{margin: "16px 16px"}}>
        <div style={{padding: 24, background: "#fff", minHeight: 360}}>
          <div className="add-button">
            <Link to={"/roster/create"}>
              <Button type="primary">
                <span>New Roster</span>
              </Button>
            </Link>
          </div>

          <Tabs
            defaultActiveKey="1"
            tabPosition={mode}
            style={{ minHeight: 220 }}
          >
            {this.createTabs.bind(this)()}
          </Tabs>

        </div>
      </Content>
    );
  }
}


export default Roster;
import React from "react";
import FormRoster from './RosterCreate'
import '../css/App.css';

import {BrowserRouter as Router, Link, Route} from 'react-router-dom';

import {Button, Divider, Form, Icon, Layout, Table, Tabs} from "antd";
import {Input, AutoComplete } from 'antd';
import { Tag } from 'antd';

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
  // render: text => <Tag color={conf.} >{text}</Tag>,
}, {
  title: 'ilvl',
  dataIndex: 'ilvl',
  key: 'ilvl',
}, {
  title: 'Class',
  dataIndex: 'fclass',
  key: 'class',
  render: text => <Tag color={getColorForClass(text)} >{text}</Tag>,
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

function getColorForClass(fclass) {
  let color;

  switch (fclass) {
    case "DeathKnight":
      color = "#C41F3B";
      break;
    case "DemonHunter":
      color = "#A330C9";
      break;
    case "Druid":
      color = "#FF7D0A";
      break;
    case "Hunter":
      color = "#ABD473";
      break;
    case "Mage":
      color = "#69CCF0";
      break;
    case "Monk":
      color = "#00FF96";
      break;
    case "Paladin":
      color = "#F58CBA";
      break;
    case "Priest":
      color = "#FFFFFF";
      break;
    case "Rogue":
      color = "#FFF569";
    break;
    case "Shaman":
      color = "#0070DE";
      break;
    case "Warlock":
      color = "#9482C9";
      break;
    case "Warrior":
      color = "#C79C6E";
    break;
    default:
      color = "#000000";
  }

  console.log(color);
  return color;
}


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
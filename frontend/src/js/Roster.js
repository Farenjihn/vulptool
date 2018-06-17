import React from "react";
import '../css/App.css';

import { Link, } from 'react-router-dom';
import {Button, Divider, Form, Icon, Layout, Table, Tabs, Tag, Avatar} from "antd";
import * as conf from "./config.js";

const FormItem = Form.Item;
const TabPane = Tabs.TabPane;
const { Content } = Layout;


const columns = [
  {
    title: 'Class',
    dataIndex: 'fclass',
    key: 'class',
    width: 80,
    sorter: (a, b) => a.fclass - b.fclass,
    render: (text, figure) => (
      <Avatar src={conf.getImgForClass(figure.fclass)} />
    ),
  }, {
    title: 'Name',
    dataIndex: 'name',
    key: 'name',
    width: 200,
    sorter: (a, b) => a.name.localeCompare(b.name),
    render: (name, figure) => (
      <Tag color={conf.getColorForClass(figure.fclass)} >{name}</Tag>
    ),
  }, {
    title: 'ilvl',
    dataIndex: 'ilvl',
    key: 'ilvl',
    sorter: (a, b) => a.ilvl - b.ilvl,
  }, {
    title: 'Player',
    dataIndex: 'player',
    key: 'player',
    sorter: (a, b) => a.player_id - b.player_id,
  },
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
            <Table columns={columns} dataSource={this.state.rosters[i].figures} pagination={false} size="small"/>
          </TabPane>
        )
      }
    }
    return tabs
  };

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
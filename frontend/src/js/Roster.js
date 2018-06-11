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

class Roster extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      mode: 'left', // position of tabs
      formVisible: false
    };
  }

  showModal = () => {
    this.setState({formVisible: true});
  };
  handleCancel = () => {
    this.setState({formVisible: false});
  };
  handleCreate = () => {
    const form = this.formRef.props.form;
    form.validateFields((err, values) => {
      if (err) {
        return;
      }

      console.log("Received values of form: ", values);

      let time_begin = values["date-picker"]
        .utc()
        .set({
          hour: values["time-begin"].get("hour"),
          minute: values["time-begin"].get("minute"),
          second: 0
        })
        .format("YYYY-MM-DD HH:mm:ss");

      let time_end = values["date-picker"]
        .utc()
        .set({
          hour: values["time-end"].get("hour"),
          minute: values["time-end"].get("minute"),
          second: 0
        })
        .format("YYYY-MM-DD HH:mm:ss");

      // console.log(time_begin);
      // console.log(time_end);

      fetch('http://localhost:9000/meeting', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          time_begin: time_begin,
          time_end: time_end
        })
      })
        .then(results => results.json())
        .then(data => (this.setState({meeting_id: data.id})))
        .catch(function (error) {
          console.log(
            "There was an error POST meeting: /// " + error + " \\\\\\"
          );
        })

        .then(
          fetch('http://localhost:9000/raid', {
            method: 'POST',
            headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/json',
            },
            body: JSON.stringify({
              name: values.raid,
              nb_boss: 0,
              difficulty: values.difficulty
            })
          })
            .then(results => results.json())
            .then(data => this.setState({raid_id: data.id}))
            .catch(function (error) {
              console.log("There was an error POST raid: /// " + error + " \\\\\\");
            }))

        .then(
          fetch('http://localhost:9000/roster', {
            method: 'POST',
            headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/json',
            },
            body: JSON.stringify({
              name: "Default test",
            })
          })
            .then(results => results.json())
            .then(data => this.setState({roster_id: data.id}))
            .catch(function (error) {
              console.log("There was an error POST raid: /// " + error + " \\\\\\");
            }))

        .then(fetch('http://localhost:9000/event', {
          method: 'POST',
          headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            name: values.title,
            description: values.description,
            meeting_id: this.state.meeting_id,
            raid_id: this.state.raid_id,
            roster_id: this.state.roster_id
          })
        })
          .then(results => results.json())
          .then(data => console.log(data))
          .catch(function (error) {
            console.log("There was an error POST event: /// " + error + " \\\\\\");
          }));


      // console.log("Meeting id " + this.state.meeting_id + ", Raid id " + this.state.raid_id + ", Roster id " + this.state.roster_id)

      form.resetFields();
      this.setState({formVisible: false});
    });
  };
  saveFormRef = formRef => {
    this.formRef = formRef;
  };

  handleCreate = () => {
    const form = this.formRef.props.form;
    form.validateFields((err, values) => {
      if (err) {
        return;
      }

      console.log("Received values of form: ", values);

      form.resetFields();
      this.setState({formVisible: false});
    });
  };

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
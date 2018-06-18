import React from 'react';
import '../css/RosterCreate.css';

import {Button, Form, Icon, Layout, Table, Input, Row, Col, Tag, Avatar} from "antd";

import * as conf from "./config.js";

const {Content} = Layout;
const FormItem = Form.Item;

let figuresid = [];

function getId(selectedRows) {
  let output = [];
  for (let f in selectedRows) {
    output.push(selectedRows[f].id);
  }
  return output;
}

const rowSelection = {
  onChange: (selectedRowKeys, selectedRows) => {
    figuresid = getId(selectedRows);
  }
};

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
];

class DynamicFieldSet extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      figures: [],
    };
  }

  handleSubmit = (e) => {
    e.preventDefault();
    const form = this.props.form;
    form.validateFields((err, values) => {
      if (err) {
        return;
      }

      console.log('Received values of form: ', values);
      console.log('Received values of form: ', figuresid);

      fetch(conf.baseURL + '/roster', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json'
        },
        body: JSON.stringify({
          name: values.name,
          figures: figuresid,
        })
      })
        .then(results => results.json())
        .catch(function (error) {
          console.log(
            "There was an error POST roster: /// " + error + " \\\\\\"
          );
        });

    });
  }

  componentDidMount() {
    fetch(conf.baseURL + '/figure', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      },
    })
      .then(results => results.json())
      .then(data => this.setState({figures:data}))
      .catch(function (error) {
        console.log(
          "There was an error GET figures: /// " + error + " \\\\\\"
        );
      });
  }

  render() {
    const { getFieldDecorator } = this.props.form;
    return (
      <Content style={{margin: "16px 16px"}}>
        <div style={{padding: 24, background: "#fff", minHeight: 360}}>
          <Form layout="vertical" onSubmit={this.handleSubmit}>
            <div className="gutter-example">
              <Row gutter={16}>
                <Col className="gutter-row" span={12}>
                  <FormItem>
                    {getFieldDecorator('name', {
                      rules: [{ required: true, message: 'Please input the name of the roster!' }],
                    })(
                      <Input prefix={<Icon type="team" style={{ color: 'rgba(0,0,0,.25)' }} />} placeholder="Name" />
                    )}
                  </FormItem>
                  <FormItem>
                    <Table columns={columns} dataSource={this.state.figures} rowSelection={rowSelection} pagination={true} size="small"/>
                  </FormItem>
                  <FormItem>
                    <Button
                      type="primary"
                      htmlType="submit"
                    >
                      Submit
                    </Button>
                  </FormItem>
                </Col>
              </Row>
            </div>
          </Form>
        </div>
      </Content>
    );
  }
}

const RosterCreate = Form.create()(DynamicFieldSet);


export default RosterCreate;
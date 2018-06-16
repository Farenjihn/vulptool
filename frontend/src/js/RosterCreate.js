import React, {Component} from 'react';
import '../css/RosterCreate.css';


import {Button, Divider, Form, Icon, Layout, Table, Tabs} from "antd";
import {Input, AutoComplete } from 'antd';
import { Row, Col } from 'antd';
import { Checkbox } from 'antd';
import { List } from 'antd';
import { Transfer } from 'antd';
import { Tag } from 'antd';

import * as conf from "./config.js";


const FormItem = Form.Item;

const TabPane = Tabs.TabPane;

const {Header, Content, Footer, Sider} = Layout;


function addToRoster(id, e) {
  if (e.target.checked) {
    this.setState({figuresID: this.state.figuresID.concat(id)});
  } else {
    let index = this.state.figuresID.indexOf(id);
    if (index > -1) {
      this.setState({figuresID: this.state.figuresID.splice(index, 1)});
    }
  }
}

class DynamicFieldSet extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      figures: [],
      figuresID: [],
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

      fetch(conf.baseURL + '/roster', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          name: values.name,
          figures: this.state.figuresID,
        })
      })
        .then(results => results.json())
        .catch(function (error) {
          console.log(
            "There was an error POST roster: /// " + error + " \\\\\\"
          );
        });

      this.setState({figuresID: []});
      form.resetFields();
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
            <FormItem>
              {getFieldDecorator('name', {
                rules: [{ required: true, message: 'Please input the name of the roster!' }],
              })(
                <Input prefix={<Icon type="team" style={{ color: 'rgba(0,0,0,.25)' }} />} placeholder="Name" />
              )}
            </FormItem>
            <FormItem>
              <div className="gutter-example">
                <Row gutter={16}>
                  <Col className="gutter-row" span={6}>
                    <List
                      size="small"
                      header={<div>Characters</div>}
                      bordered
                      dataSource={this.state.figures}
                      renderItem={item => (<List.Item><Checkbox onChange={(e) => addToRoster.bind(this)(item.id, e)}>{item.name}</Checkbox></List.Item>)}
                    />
                  </Col>
                </Row>
              </div>
            </FormItem>
            <FormItem>
              <Button
                type="primary"
                htmlType="submit"
              >
                Submit
              </Button>
            </FormItem>
          </Form>
        </div>
      </Content>
    );
  }
}

const RosterCreate = Form.create()(DynamicFieldSet);


export default RosterCreate;
import React from "react";
import '../css/RosterCreate.css';

import {BrowserRouter as Router, Link, Route} from 'react-router-dom';

import {Button, Divider, Form, Icon, Layout, Table, Tabs} from "antd";
import {Input, AutoComplete } from 'antd';
import { Row, Col } from 'antd';
import { List } from 'antd';
import { Transfer } from 'antd';

import * as conf from "./config.js";


const FormItem = Form.Item;

const TabPane = Tabs.TabPane;

const {Header, Content, Footer, Sider} = Layout;

const Option = AutoComplete.Option;
const OptGroup = AutoComplete.OptGroup;


let uuid = 0;
class DynamicFieldSet extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      figures: [],
      mockData: [],
      targetKeys: [],
    };
  }

  getMock = () => {
    const targetKeys = [];
    const mockData = [];
    for (let i = 0; i < 20; i++) {
      const data = {
        key: i.toString(),
        title: `content${i + 1}`,
        description: `description of content${i + 1}`,
        chosen: Math.random() * 2 > 1,
      };
      if (data.chosen) {
        targetKeys.push(data.key);
      }
      mockData.push(data);
    }
    this.setState({ mockData, targetKeys });
  }
  handleChange = (targetKeys, direction, moveKeys) => {
    console.log(targetKeys, direction, moveKeys);
    this.setState({ targetKeys });
  }
  renderItem = (item) => {
    const customLabel = (
      <span className="custom-item">
        {item.title} - {item.description}
      </span>
    );

    return {
      label: customLabel, // for displayed item
      value: item.title, // for title and filter matching
    };
  }

  remove = (k) => {
    const { form } = this.props;
    // can use data-binding to get
    const keys = form.getFieldValue('keys');
    // We need at least one passenger
    if (keys.length === 1) {
      return;
    }

    // can use data-binding to set
    form.setFieldsValue({
      keys: keys.filter(key => key !== k),
    });
  }

  add = () => {
    const { form } = this.props;
    // can use data-binding to get
    const keys = form.getFieldValue('keys');
    const nextKeys = keys.concat(uuid);
    uuid++;
    // can use data-binding to set
    // important! notify form to detect changes
    form.setFieldsValue({
      keys: nextKeys,
    });
  }

  handleSubmit = (e) => {
    e.preventDefault();
    this.props.form.validateFields((err, values) => {
      if (!err) {
        console.log('Received values of form: ', values);
      }
    });
  }

  componentDidMount() {
    this.getMock();

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
    const { getFieldDecorator, getFieldValue } = this.props.form;
    const formItemLayout = {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 4 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 20 },
      },
    };
    const formItemLayoutWithOutLabel = {
      wrapperCol: {
        xs: { span: 24, offset: 0 },
        sm: { span: 20, offset: 4 },
      },
    };
    getFieldDecorator('keys', { initialValue: [] });
    const keys = getFieldValue('keys');
    const formItems = keys.map((k, index) => {
      return (
        <FormItem
          {...(index === 0 ? formItemLayout : formItemLayoutWithOutLabel)}
          label={index === 0 ? 'Characters' : ''}
          required={false}
          key={k}
        >
          {getFieldDecorator(`figures[${k}]`, {
            validateTrigger: ['onChange', 'onBlur'],
            rules: [{
              required: true,
              whitespace: true,
              message: "Please input character's name or delete this field.",
            }],
          })(
            <AutoComplete
              style={{width: '60%', marginRight: 8}}
              placeholder="character name"
              filterOption={(inputValue, option) => option.props.children.toUpperCase().indexOf(inputValue.toUpperCase()) !== -1}
            />
          )}
          {keys.length > 1 ? (
            <Icon
              className="dynamic-delete-button"
              type="minus-circle-o"
              disabled={keys.length === 1}
              onClick={() => this.remove(k)}
            />
          ) : null}
        </FormItem>
      );
    });
    return (
      <Content style={{margin: "16px 16px"}}>
        <div style={{padding: 24, background: "#fff", minHeight: 360}}>
          <div className="gutter-example">
            <Row gutter={32}>
              <Col className="gutter-row" span={6}>
                <Form onSubmit={this.handleSubmit}>
                  <FormItem label="Name">
                    {getFieldDecorator('name', {
                      rules: [{required: true, message: 'Please input a name for the roster!'}],
                    })(
                      <Input/>
                    )}
                  </FormItem>
                  {formItems}
                  <FormItem {...formItemLayoutWithOutLabel}>
                    <Button type="dashed" onClick={this.add} style={{width: '60%'}}>
                      <Icon type="plus"/> Add field
                    </Button>
                  </FormItem>
                  <FormItem {...formItemLayoutWithOutLabel}>
                    <Button type="primary" htmlType="submit">Submit</Button>
                  </FormItem>
                </Form>
              </Col>
              <Col className="gutter-row" span={6}>
                <List
                  size="small"
                  header={<div>Header</div>}
                  footer={<div>Footer</div>}
                  bordered
                  dataSource={this.state.figures}
                  renderItem={item => (<List.Item>{item.name}</List.Item>)}
                />
              </Col>
              <Col className="gutter-row" span={12}>

                <Transfer
                  dataSource={this.state.mockData}
                  listStyle={{
                    width: 300,
                  }}
                  targetKeys={this.state.targetKeys}
                  onChange={this.handleChange}
                  render={this.renderItem}
                />
              </Col>

            </Row>
          </div>

        </div>
      </Content>
    );
  }
}

const RosterCreate = Form.create()(DynamicFieldSet);


export default RosterCreate;
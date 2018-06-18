import React from "react";
import { Form, Input, Tooltip, Icon, Row, Col, Button } from 'antd';
import {Layout} from "antd/lib/index";
import * as conf from "./config";

const FormItem = Form.Item;

const {Content} = Layout;


class RegistrationForm extends React.Component {
  state = {
    confirmDirty: false,
    autoCompleteResult: [],
  };
  handleSubmit = (e) => {
    e.preventDefault();
    this.props.form.validateFieldsAndScroll((err, values) => {
      if (!err) {
        console.log('Received values of form: ', values);

        fetch(conf.baseURL + '/player', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
          },
          body: JSON.stringify({
            main_pseudo: values.username,
            password: values.password,
          })
        })
          .then(results => results.json())
          .catch(function (error) {
            console.log(
              "There was an error POST Player: /// " + error + " \\\\\\"
            );
          });
      }
    });
  };
  handleConfirmBlur = (e) => {
    const value = e.target.value;
    this.setState({ confirmDirty: this.state.confirmDirty || !!value });
  };
  compareToFirstPassword = (rule, value, callback) => {
    const form = this.props.form;
    if (value && value !== form.getFieldValue('password')) {
      callback('Two passwords that you enter is inconsistent!');
    } else {
      callback();
    }
  };
  validateToNextPassword = (rule, value, callback) => {
    const form = this.props.form;
    if (value && this.state.confirmDirty) {
      form.validateFields(['confirm'], { force: true });
    }
    callback();
  };

  render() {
    const { getFieldDecorator } = this.props.form;

    const formItemLayout = {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 8 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
    };
    const tailFormItemLayout = {
      wrapperCol: {
        xs: {
          span: 24,
          offset: 0,
        },
        sm: {
          span: 16,
          offset: 8,
        },
      },
    };

    return (
      <Content style={{margin: "16px 16px"}}>
        <div style={{padding: 24, background: "#fff", minHeight: 360}}>
          <Row gutter={16}>
            <Col className="gutter-row" span={8}>
              <Form onSubmit={this.handleSubmit}>
                <FormItem
                  {...formItemLayout}
                  label={(
                    <span>
              Username&nbsp;
                      <Tooltip title="What do you want others to call you?">
                <Icon type="question-circle-o" />
              </Tooltip>
            </span>
                  )}
                >
                  {getFieldDecorator('username', {
                    rules: [{ required: true, message: 'Please input your nickname!', whitespace: true }],
                  })(
                    <Input prefix={<Icon type="user" style={{ color: 'rgba(0,0,0,.25)' }} />}  />
                  )}
                </FormItem>
                <FormItem
                  {...formItemLayout}
                  label="Password"
                >
                  {getFieldDecorator('password', {
                    rules: [{
                      required: true, message: 'Please input your password!',
                    }, {
                      validator: this.validateToNextPassword,
                    }],
                  })(
                    <Input prefix={<Icon type="lock" style={{ color: 'rgba(0,0,0,.25)' }} />} type="password"  />
                  )}
                </FormItem>
                <FormItem
                  {...formItemLayout}
                  label="Confirm Password"
                >
                  {getFieldDecorator('confirm', {
                    rules: [{
                      required: true, message: 'Please confirm your password!',
                    }, {
                      validator: this.compareToFirstPassword,
                    }],
                  })(
                    <Input prefix={<Icon type="lock" style={{ color: 'rgba(0,0,0,.25)' }} />} type="password" onBlur={this.handleConfirmBlur} />
                  )}
                </FormItem>

                <FormItem
                  {...formItemLayout}
                  label="Discord ID"
                >
                  {getFieldDecorator('discord', {
                    rules: [{ required: true, message: 'Please input your Discord ID!' }],
                  })(
                      <Input placeholder="User#0000"/>
                  )}
                </FormItem>
                <FormItem {...tailFormItemLayout}>
                  <Button type="primary" htmlType="submit">Register</Button>
                </FormItem>
              </Form>
            </Col>
          </Row>
        </div>
      </Content>
    );
  }
}

const Register = Form.create()(RegistrationForm);

export default Register


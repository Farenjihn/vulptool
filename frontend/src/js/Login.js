import React from "react";
import '../css/Login.css';

import { Form, Icon, Input, Button, Checkbox, Layout, Row, Col } from 'antd';
import * as conf from "./config";

const FormItem = Form.Item;
const {Content} = Layout;



class NormalLoginForm extends React.Component {
  handleSubmit = (e) => {
    e.preventDefault();
    this.props.form.validateFields((err, values) => {
      if (!err) {
        console.log('Received values of form: ', values);

        fetch(conf.baseURL + '/player/login', {
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
              "There was an error POST Player/login: /// " + error + " \\\\\\"
            );
          });
      }
    });
  }
  render() {
    const { getFieldDecorator } = this.props.form;
    return (
      <Content style={{margin: "16px 16px"}}>
        <div style={{padding: 24, background: "#fff", minHeight: 360}}>
          <Row gutter={16}>
            <Col className="gutter-row" span={6}>
              <Form onSubmit={this.handleSubmit} className="login-form">
                <FormItem>
                  {getFieldDecorator('userName', {
                    rules: [{ required: true, message: 'Please input your username!' }],
                  })(
                    <Input prefix={<Icon type="user" style={{ color: 'rgba(0,0,0,.25)' }} />} placeholder="Username" />
                  )}
                </FormItem>
                <FormItem>
                  {getFieldDecorator('password', {
                    rules: [{ required: true, message: 'Please input your Password!' }],
                  })(
                    <Input prefix={<Icon type="lock" style={{ color: 'rgba(0,0,0,.25)' }} />} type="password" placeholder="Password" />
                  )}
                </FormItem>
                <FormItem>
                  {getFieldDecorator('remember', {
                    valuePropName: 'checked',
                    initialValue: true,
                  })(
                    <Checkbox>Remember me</Checkbox>
                  )}
                  <a className="login-form-forgot" href="">Forgot password</a>
                  <Button type="primary" htmlType="submit" className="login-form-button">
                    Log in
                  </Button>
                  Or <a href="">register now!</a>
                </FormItem>
              </Form>
            </Col>
          </Row>
        </div>
      </Content>
    );
  }
}

const Login = Form.create()(NormalLoginForm);

export default Login;


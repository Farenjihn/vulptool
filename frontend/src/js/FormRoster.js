import React from "react";

import '../css/FormRoster.css';

import {Button, Form, Icon, Input, Modal} from 'antd';

const FormItem = Form.Item;


let uuid = 0;

const FormRoster = Form.create()(
  class FormRoster extends React.Component {
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

    render() {
      const { getFieldDecorator, getFieldValue } = this.props.form;
      const {visible, onCancel, onCreate, form} = this.props;
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
              <Input placeholder="character name" style={{ width: '60%', marginRight: 8 }} />
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
        <Modal
          visible={visible}
          title="Create a new roster"
          okText="Create"
          onCancel={onCancel}
          onOk={onCreate}
        >
          <Form>
            <FormItem label="Name">
              {getFieldDecorator('name', {
                rules: [{required: true, message: 'Please input a name for the roster!'}],
              })(
                <Input/>
              )}
            </FormItem>
            {formItems}
            <FormItem {...formItemLayoutWithOutLabel}>
              <Button type="dashed" onClick={this.add} style={{ width: '60%' }}>
                <Icon type="plus" /> Add field
              </Button>
            </FormItem>
          </Form>
        </Modal>
      );
    }
  }
);


export default FormRoster;
import React from "react";

import {DatePicker, Form, Input, Modal, Radio, Select, TimePicker} from 'antd';
import moment from 'moment';

const formatDate = 'YYYY-MM-DD';
const formatTime = 'HH:mm';
const FormItem = Form.Item;
const Option = Select.Option;
const RadioButton = Radio.Button;
const RadioGroup = Radio.Group;

const FormRoster = Form.create()(
  class extends React.Component {
    render() {
      const {visible, onCancel, onCreate, form} = this.props;
      const {getFieldDecorator} = form;

      return (
        <Modal
          visible={visible}
          title="Create a new event"
          okText="Create"
          onCancel={onCancel}
          onOk={onCreate}
        >
          <Form layout="horizontal">
            <FormItem label="Title">
              {getFieldDecorator('title', {
                rules: [{required: true, message: 'Please input the title of the event!'}],
              })(
                <Input/>
              )}
            </FormItem>
            <FormItem label="Description">
              {getFieldDecorator('description')(<Input type="textarea"/>)}
            </FormItem>

            <FormItem label="Raid">
              {getFieldDecorator('raid', {
                rules: [{required: true, message: 'Please select a raid!'}],
                initialValue: 't21'
              })(
                <Select placeholder="Raid" onChange={this.handleSelectChange}>
                  <Option value="t21">Antorus the Buring Throne</Option>
                  <Option value="t20">Tomb of Sargeras</Option>
                </Select>
              )}
            </FormItem>
            <FormItem className="collection-create-form_last-form-item">
              {getFieldDecorator('difficulty', {
                initialValue: 'Mythic',
              })(
                <Radio.Group>
                  <RadioButton value="RaidFinder">Raid Finder</RadioButton>
                  <RadioButton value="Normal">Normal</RadioButton>
                  <RadioButton value="Heroic">Heroic</RadioButton>
                  <RadioButton value="Mythic">Mythic</RadioButton>
                </Radio.Group>
              )}
            </FormItem>

            <FormItem label="Date">
              {getFieldDecorator('date-picker', {
                rules: [{type: 'object', required: true, message: 'Please select date!'}],
                initialValue: moment(new Date(), formatDate),

              })(
                <DatePicker format={formatDate}/>
              )}
            </FormItem>
            <FormItem label="Start Time">
              {getFieldDecorator('time-begin', {
                rules: [{type: 'object', required: true, message: 'Please select time!'}],
                initialValue: moment('20:30', formatTime),
              })(
                <TimePicker format={formatTime} minuteStep={10}/>
              )}
            </FormItem>
            <FormItem label="End Time">
              {getFieldDecorator('time-end', {
                rules: [{type: 'object', required: true, message: 'Please select time!'}],
                initialValue: moment('23:50', formatTime),
              })(
                <TimePicker format={formatTime} minuteStep={10}/>
              )}
            </FormItem>

          </Form>
        </Modal>
      );
    }
  }
);

export default FormRoster;
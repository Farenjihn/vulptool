import React, { Component } from 'react';
import '../css/RosterCreate.css';

import { Button, DatePicker, Form, Input, Radio, Select, TimePicker, Layout, Tabs } from 'antd';
import moment from 'moment';

import * as conf from "./config.js";

const formatDate = 'YYYY-MM-DD';
const formatTime = 'HH:mm';
const FormItem = Form.Item;
const Option = Select.Option;
const RadioButton = Radio.Button;

const { Content } = Layout;

class DynamicFieldSet extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
    };
  }

  handleSubmit = (e) => {
    e.preventDefault();
    const form = this.props.form;
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
        .unix();
      let time_end = values["date-picker"]
        .utc()
        .set({
          hour: values["time-end"].get("hour"),
          minute: values["time-end"].get("minute"),
          second: 0
        })
        .unix();

        console.log(time_begin + " " + time_end)
      fetch(conf.baseURL + '/event/' + this.state.event.id, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          id: this.state.event.id,
          name: values.title,
          description: values.description,
          meeting: {
            id: this.state.event.meeting.id,
            time_begin: time_begin + "",
            time_end: time_end + ""
          },
          raid: {
            id: this.state.event.raid.id,
            name: values.raid,
            nb_boss: this.state.event.raid.nb_boss,
            difficulty: values.difficulty
          },
          roster: {
            id: this.state.event.roster.id,
            name: this.state.event.roster.name
          }
        })
      })
        //.then(results => window.alert(JSON.stringify(results.json(), null, 4)))
        .then(results => console.log(results))
        .catch(function (error) {
          console.log(
            "There was an error POST meeting: /// " + error + " \\\\\\"
          );
        });
    });
  }

  componentDidMount() {
    fetch(conf.baseURL + '/event/' + this.props.match.params.id, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      },
    })
      .then(results => results.json())
      .then(data => this.setState({ event: data }))
      .catch(function (error) {
        console.log(
          "There was an error GET event: /// " + error + " \\\\\\"
        );
      });
  }

  render() {
    const { getFieldDecorator } = this.props.form;
    const e = this.state.event
    return (
      <Content style={{ margin: "16px 16px" }}>
        <div style={{ padding: 24, background: "#fff", minHeight: 360 }}>
          <Form layout="horizontal" onSubmit={this.handleSubmit}>
            <FormItem label="Title">
              {getFieldDecorator('title', {
                rules: [{ required: true, message: 'Please input the title of the event!' }],
                initialValue: (e ? e.name : '')
              })(
                <Input />
              )}
            </FormItem>
            <FormItem label="Description">
              {getFieldDecorator('description', {
                initialValue: (e ? e.description : '')
              })(<Input type="textarea" />)}
            </FormItem>

            <FormItem label="Raid">
              {getFieldDecorator('raid', {
                rules: [{ required: true, message: 'Please select a raid!' }],
                initialValue: (e ? e.raid.name : 't21')
              })(
                <Select placeholder="Raid" onChange={this.handleSelectChange}>
                  <Option value="t21">Antorus the Buring Throne</Option>
                  <Option value="t20">Tomb of Sargeras</Option>
                </Select>
              )}
            </FormItem>
            <FormItem className="collection-create-form_last-form-item">
              {getFieldDecorator('difficulty', {
                initialValue: (e ? e.raid.difficulty : ''),
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
                rules: [{ type: 'object', required: true, message: 'Please select date!' }],
                initialValue: (e ? moment.unix(e.meeting.time_begin) : moment(new Date(), formatDate)),

              })(
                <DatePicker format={formatDate} />
              )}
            </FormItem>
            <FormItem label="Start Time">
              {getFieldDecorator('time-begin', {
                rules: [{ type: 'object', required: true, message: 'Please select time!' }],
                initialValue: (e ? moment.unix(e.meeting.time_begin) : moment('20:30', formatTime)),
              })(
                <TimePicker format={formatTime} minuteStep={10} />
              )}
            </FormItem>
            <FormItem label="End Time">
              {getFieldDecorator('time-end', {
                rules: [{ type: 'object', required: true, message: 'Please select time!' }],
                initialValue: (e ? moment.unix(e.meeting.time_end) : moment('23:50', formatTime)),
              })(
                <TimePicker format={formatTime} minuteStep={10} />
              )}
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
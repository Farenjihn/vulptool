import React from "react";

import { Button, Modal, Form, Input, Radio } from 'antd';
import {
    Select, InputNumber, Switch,
    Slider, Upload, Icon, Rate,
} from 'antd';
import { DatePicker } from 'antd';
import { TimePicker } from 'antd';
import moment from 'moment';

const format = 'HH:mm';
const FormItem = Form.Item;
const Option = Select.Option;
const RadioButton = Radio.Button;
const RadioGroup = Radio.Group;

const EventFrom = Form.create()(
    class extends React.Component {
        render() {
            const { visible, onCancel, onCreate, form } = this.props;
            const { getFieldDecorator } = form;

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
                                rules: [{ required: true, message: 'Please input the title of the event!' }],
                            })(
                                <Input />
                            )}
                        </FormItem>
                        <FormItem label="Description">
                            {getFieldDecorator('description')(<Input type="textarea" />)}
                        </FormItem>

                        <FormItem label="Raid">
                            {getFieldDecorator('raid', {
                                rules: [{ required: true, message: 'Please select a raid!' }],
                            })(
                                <Select placeholder="Raid" onChange={this.handleSelectChange}>
                                    <Option value="t21">Antorus the Buring Throne</Option>
                                    <Option value="t20">Tomb of Sargeras</Option>
                                </Select>
                            )}
                        </FormItem>

                        <FormItem label="Date">
                            {getFieldDecorator('date-picker', {
                                rules: [{ type: 'object', required: true, message: 'Please select date!' }],
                            })(
                                <DatePicker />
                            )}
                        </FormItem>
                        <FormItem label="Start Time">
                            {getFieldDecorator('time-begin', {
                                rules: [{ type: 'object', required: true, message: 'Please select time!' }],
                                initialValue: moment('20:30', format),
                            })(
                                <TimePicker format={format} minuteStep={10}/>
                            )}
                        </FormItem>
                        <FormItem label="End Time">
                            {getFieldDecorator('time-end', {
                                rules: [{ type: 'object', required: true, message: 'Please select time!' }],
                                initialValue: moment('23:50', format),
                            })(
                                <TimePicker format={format} minuteStep={10}/>
                            )}
                        </FormItem>



                        <FormItem className="collection-create-form_last-form-item">
                            {getFieldDecorator('modifier', {
                                initialValue: 'mythic mode',
                            })(
                                <Radio.Group>
                                    <RadioButton value="normal mode">Normal Mode</RadioButton>
                                    <RadioButton value="hard mode">Hard Mode</RadioButton>
                                    <RadioButton value="mythic mode">Mythic Mode</RadioButton>
                                </Radio.Group>
                            )}
                        </FormItem>
                    </Form>
                </Modal>
            );
        }
    }
);

export default EventFrom;
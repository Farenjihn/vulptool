import React from "react";

import { Button, Modal, Form, Input, Radio } from 'antd';
import {
    Select, InputNumber, Switch,
    Slider, Upload, Icon, Rate,
} from 'antd';

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
                    <Form layout="vertical">
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
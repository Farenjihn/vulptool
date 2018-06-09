import React from "react";
import '../css/Calendar.css';
import EventForm from './EventForm';

import { Layout, Menu, Breadcrumb, Icon } from 'antd';
import { List, Row, Col } from 'antd';
import { Button, Card, Avatar } from 'antd';
import { DatePicker } from 'antd';
import moment from 'moment';

const RangePicker = DatePicker.RangePicker;
const WeekPicker = DatePicker.WeekPicker;

const { Meta } = Card;
const { Header, Content, Footer, Sider } = Layout;
const SubMenu = Menu.SubMenu;
const IconText = ({ type, text }) => (
    <span>
    <Icon type={type} style={{ marginRight: 8 }} />
        {text}
  </span>
);

moment.locale('en-wow-settings', {
    week: {
        dow: 3
    }
});

const listData = [];
for (let i = 0; i < 4; i++) {
    listData.push({
        href: 'http://ant.design',
        title: `RAID bla bla ${i}`,
        avatar: 'https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png',
        description: 'Ant Design, a design language for background applications, is refined by Ant UED Team.',
        content: 'We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure), to help people create their product prototypes beautifully and efficiently.',
    });
}

function onChange(date, dateString) {
    console.log(date, dateString);
}

class Calendar extends React.Component {
    constructor() {
        super();
        this.state = {
            meetings: [],
            formVisible: false
        };
    }

    componentDidMount() {
        fetch('http://localhost:9000/meeting', {
            method: 'GET',
        })
            .then(results => results.json())
            .then(data => this.setState({ meetings: data }))
            .catch(function (error) {
                console.log("There was an error Fetching data: /// " + error + " \\\\\\");
            });

    }

    showModal = () => {
        this.setState({ formVisible: true });
    }
    handleCancel = () => {
        this.setState({ formVisible: false });
    }
    handleCreate = () => {
        const form = this.formRef.props.form;
        form.validateFields((err, values) => {
            if (err) {
                return;
            }

            console.log('Received values of form: ', values);


            let time_begin = values["date-picker"].utc().set({
                'hour': values["time-begin"].get('hour'),
                'minute': values["time-begin"].get('minute'),
                'second': 0
            }).unix();
            let time_end = values["date-picker"].utc().set({
                'hour': values["time-end"].get('hour'),
                'minute': values["time-end"].get('minute'),
                'second': 0
            }).unix();

            let meeting_id;
            fetch('http://localhost:9000/meeting', {
                method: 'POST',
                headers: {
                    // 'Accept': 'application/json',
                    // 'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    time_begin: time_begin,
                    time_end: time_end,
                })
            })
                .then(results => results.json())
                .then(data => meeting_id = data.id)
                .catch(function (error) {
                    console.log("There was an error POST meeting: /// " + error + " \\\\\\");
                });

            console.log(meeting_id);

            /*let raid_id;
            fetch('http://localhost:9000/raid', {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                }, 
                body: JSON.stringify({
                    name: values.raid,
                    nb_boss: 0,
                    difficulty: values.difficulty
                })
            })
                .then(results => results.json())
                .then(data => raid_id = data.id)
                .catch(function (error) {
                    console.log("There was an error POST raid: /// " + error + " \\\\\\");
                });*/

            /*let roster_id;
            fetch('http://localhost:9000/roster', {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    name: "Default test",
                })
            })
                .then(results => results.json())
                .then(data => roster_id = data.id)
                .catch(function (error) {
                    console.log("There was an error POST raid: /// " + error + " \\\\\\");
                });

            fetch('http://localhost:9000/event', {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    name: values.title,
                    description: values.description,
                    meeting_id: meeting_id,
                    raid_id: 0,
                    roster_id: roster_id
                })
            })
                .then(results => results.json())
                .then(data => console.log(data))
                .catch(function (error) {
                    console.log("There was an error POST event: /// " + error + " \\\\\\");
                });*/

            form.resetFields();
            this.setState({ formVisible: false });
        });
    }
    saveFormRef = (formRef) => {
        this.formRef = formRef;
    }

    render() {
        return (
            <Content style={{ margin: '16px 16px' }}>
                <div style={{ padding: 24, background: '#fff', minHeight: 360 }}>

                    <div>
                        <WeekPicker onChange={onChange} placeholder="Select week"/>
                    </div>


                    <div>
                        <Button type="primary" onClick={this.showModal}>New Event</Button>
                        <EventForm
                            wrappedComponentRef={this.saveFormRef}
                            visible={this.state.formVisible}
                            onCancel={this.handleCancel}
                            onCreate={this.handleCreate}
                        />
                    </div>


                    <List
                        itemLayout="vertical"
                        size="large"
                        pagination={{
                            onChange: (page) => {
                                console.log(page);
                            },
                            pageSize: 3,
                        }}
                        dataSource={listData}
                        renderItem={item => (
                            <List.Item
                                key={item.title}
                                actions={[<IconText type="star-o" text="156" />, <IconText type="like-o" text="156" />, <IconText type="message" text="2" />]}
                                extra={<img width={272} alt="logo" src="https://gw.alipayobjects.com/zos/rmsportal/mqaQswcyDLcXyDKnZfES.png" />}
                            >
                                <List.Item.Meta
                                    avatar={<Avatar src={item.avatar} />}
                                    title={<a href={item.href}>{item.title}</a>}
                                    description={item.description}
                                />
                                {item.content}
                            </List.Item>
                        )}
                    />
                </div>
            </Content>
        );
    }
}

export default Calendar;

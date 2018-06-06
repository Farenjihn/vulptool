import React from "react";
import './Calendar.css';

import { Layout, Menu, Breadcrumb, Icon } from 'antd';
import { List, Row, Col } from 'antd';
import { Card, Avatar } from 'antd';
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
    state = {
        meetings : [],
        firstDayOfWeek: 0
    };

    componentDidMount() {
        fetch(`/api/meeting`)
            .then(result=> {
                this.setState({items:result.json()});
            });
    }

    render() {
        return (
            <Content style={{ margin: '16px 16px' }}>
                <div style={{ padding: 24, background: '#fff', minHeight: 360 }}>
                    {/*<div className="gutter-example">
                        <Row gutter={16}>
                            <Col className="gutter-row" span={3}>
                                <div className="gutter-box">Wednesday</div>

                            </Col>
                            <Col className="gutter-row" span={3}>
                                <div className="gutter-box">Thursday</div>
                                <Card
                                    //style={{ width: 300 }}
                                    cover={<img alt="example" src="https://gw.alipayobjects.com/zos/rmsportal/JiqGstEfoWAOHiTxclqi.png" />}
                                    actions={[<Icon type="setting" />, <Icon type="edit" />, <Icon type="ellipsis" />]}
                                >
                                    <Meta
                                        avatar={<Avatar src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png" />}
                                        title="RAID X"
                                        description="This is the description"
                                    />
                                </Card>
                            </Col>
                            <Col className="gutter-row" span={3}>
                                <div className="gutter-box">Friday</div>
                            </Col>
                            <Col className="gutter-row" span={3}>
                                <div className="gutter-box">Saturday</div>
                            </Col>
                            <Col className="gutter-row" span={3}>
                                <div className="gutter-box">Sunday</div>
                            </Col>
                            <Col className="gutter-row" span={3}>
                                <div className="gutter-box">Monday</div>
                            </Col>
                            <Col className="gutter-row" span={3}>
                                <div className="gutter-box">Tuesday</div>
                            </Col>
                            <Col className="gutter-row" span={3}>
                                <div className="gutter-box">Rosters</div>
                            </Col>
                        </Row>
                    </div>*/}

                    <div>
                        <WeekPicker onChange={onChange} placeholder="Select week"/>
                    </div>

                    <div>
                        {JSON.stringify(this.state.meetings)}
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

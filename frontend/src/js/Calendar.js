import React from "react";
import "../css/Calendar.css";
import EventForm from "./FormEvent";

import {Avatar, Button, Card, DatePicker, Icon, Layout, List, Menu} from "antd";
import moment from "moment";

const RangePicker = DatePicker.RangePicker;
const WeekPicker = DatePicker.WeekPicker;

const {Meta} = Card;
const {Header, Content, Footer, Sider} = Layout;
const SubMenu = Menu.SubMenu;
const IconText = ({type, text}) => (
  <span>
    <Icon type={type} style={{marginRight: 8}}/>
    {text}
  </span>
);

moment.locale("en-wow-settings", {
  week: {
    dow: 3
  }
});

const listData = [];
for (let i = 0; i < 4; i++) {
  listData.push({
    href: "http://ant.design",
    title: `RAID bla bla ${i}`,
    avatar: "https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png",
    description:
      "Ant Design, a design language for background applications, is refined by Ant UED Team.",
    content:
      "We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure), to help people create their product prototypes beautifully and efficiently."
  });
}

var displaytWeek = moment();

// check if an element exists in array using a comparer function
// comparer : function(currentElement)
Array.prototype.inArray = function(comparer) {
  for(var i=0; i < this.length; i++) {
    if(comparer(this[i])) return true;
  }
  return false;
};

// adds an element to the array if it does not already exist using a comparer
// function
Array.prototype.pushIfNotExist = function(element, comparer) {
  if (!this.inArray(comparer)) {
    this.push(element);
  }
};

function nextWeek() {
  console.log("Next Week called");
  weekPickerChange(displaytWeek.add(7, "days"))
}

function previousWeek() {
  console.log("Previous Week called");
  weekPickerChange(displaytWeek.subtract(7, "days"))
}

function weekPickerChange(date, dateString) {
  displaytWeek = moment().hour(0).minute(0).second(0).day("Wednesday").week(date.week()).utc(true);

  let url = "http://localhost:9000/eventByDate";
  url = url + "/" + moment().hour(0).minute(0).second(0).day("Wednesday").week(date.week()).utc(true).unix() + "/" + moment().hour(23).minute(59).second(59).day("Tuesday").week(date.week()).utc(true).unix();

  fetch(url, {
    method: "GET",
  })
    .then(results => results.json())
    .then(data => {
      let meetingsCopy = this.state.meetings;
      console.log(meetingsCopy);

      if (data.length > 0) {
        for (let meeting in data) {
          meetingsCopy.pushIfNotExist(meeting, function (e) {
            return e.id === meeting.id;
          });
        }
      }

      console.log(meetingsCopy);
    })
    .catch(function (error) {
      console.log(
        "There was an error Fetching data: /// " + error + " \\\\\\"
      );
    });
}

class Calendar extends React.Component {
  constructor() {
    super();
    this.state = {
      meetings: [],
      formVisible: false
    };
  }

  showModal = () => {
    this.setState({formVisible: true});
  };
  handleCancel = () => {
    this.setState({formVisible: false});
  };
  handleCreate = () => {
    const form = this.formRef.props.form;
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
        .format("YYYY-MM-DD HH:mm:ss");

      let time_end = values["date-picker"]
        .utc()
        .set({
          hour: values["time-end"].get("hour"),
          minute: values["time-end"].get("minute"),
          second: 0
        })
        .format("YYYY-MM-DD hh:mm:ss");


      fetch('http://localhost:9000/meeting', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          time_begin: time_begin,
          time_end: time_end
        })
      })
        .then(results => results.json())
        .then(data => (this.setState({meeting_id: data.id})))
        .catch(function (error) {
          console.log(
            "There was an error POST meeting: /// " + error + " \\\\\\"
          );
        });

      form.resetFields();
      this.setState({formVisible: false});
    });
  };
  saveFormRef = formRef => {
    this.formRef = formRef;
  };


  componentDidMount() {
    let url = "http://localhost:9000/eventByDate";
    url = url + "/" + moment().hour(0).minute(0).second(0).day("Wednesday").week(moment().week()).utc(true).unix() + "/" + moment().hour(23).minute(59).second(59).day("Thuesday").week(moment().week()).utc(true).unix();

    console.log(url);
    console.log(moment("1529020799", "X").format("dddd Do MMMM YYYY"));

    fetch(url, {
      method: "GET",
    })
      .then(results => results.json())
      .then(data => this.setState({meetings: data}))
      .catch(function (error) {
        console.log(
          "There was an error Fetching data: /// " + error + " \\\\\\"
        );
      });
  }

  render() {
    return (
      <Content style={{margin: "16px 16px"}}>
        <div style={{padding: 24, background: "#fff", minHeight: 360}}>

          <div className="footer-container">
            <div className="div-left">
              <ul className="footer">
                <Button.Group>
                  <Button onClick={previousWeek.bind(this)} icon="left"/>
                  <WeekPicker id="weekpicker" onChange={weekPickerChange.bind(this)} placeholder={moment().day("Wednesday").utc(true).format("Do MMM YY")}/>
                  <Button onClick={nextWeek.bind(this)} icon="right"/>
                </Button.Group>
              </ul>
            </div>
            <div className="div-right">
              <ul className="footer">
                <Button type="primary" onClick={this.showModal}>
                  New Event
                </Button>
                <EventForm
                  wrappedComponentRef={this.saveFormRef}
                  visible={this.state.formVisible}
                  onCancel={this.handleCancel}
                  onCreate={this.handleCreate}
                />
              </ul>
            </div>
          </div>



          <List
            itemLayout="vertical"
            size="large"
            pagination={{
              onChange: page => {
                console.log(page);
              },
              pageSize: 5
            }}
            dataSource={this.state.meetings}
            renderItem={item => (
              <List.Item
                key={item.id}
                /*actions={[
                  <IconText type="star-o" text="156"/>,
                  <IconText type="like-o" text="156"/>,
                  <IconText type="message" text="2"/>
                ]}*/
                extra={
                  <img
                    width={272}
                    alt="logo"
                    src="https://gw.alipayobjects.com/zos/rmsportal/mqaQswcyDLcXyDKnZfES.png"
                  />
                }
              >
                <List.Item.Meta
                  avatar={<Avatar src={item.avatar}/>}
                  title={<a href={item.href}>{item.name} - {moment(item.meeting.time_begin/1000, "X").format("dddd Do MMMM YYYY hh:mm")}</a>}
                  description={<div>{item.raid.name} - {item.raid.difficulty}</div>}
                />

                <div>
                  <p>{item.description}</p>

                  <p><b>Roster :</b> {item.roster.name}</p>

                  {/*<List
                    size="small"
                    header={<div>Rosters</div>}
                    dataSource={item.roster}
                    renderItem={rosterList => (<List.Item>{rosterList.name}</List.Item>)}
                  />*/}

                </div>
              </List.Item>
            )}
          />
        </div>
      </Content>
    );
  }
}

export default Calendar;

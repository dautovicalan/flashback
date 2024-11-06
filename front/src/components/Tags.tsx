type TagsProps = {
  tags?: string[];
};

const Tags = ({ tags }: TagsProps) => {
  return (
    <div>
      <p>Tags</p>

      {tags &&
        tags.map((tag, index) => (
          <span key={index} className="badge badge-primary mr-2">
            {tag}
          </span>
        ))}
    </div>
  );
};

export default Tags;
